package cn.yunyichina.log.store.node.server;

import cn.yunyichina.log.common.util.UnZip;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.json.JSONException;
import org.json.JSONObject;

import javax.activation.MimetypesFileTypeMap;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Jonven on 2016/11/21.
 */
public class HttpStaticFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    // where to store the files
    private static final String BASE_PATH = "E://uploads/";

    // query param used to download a file
    private static final String FILE_QUERY_PARAM = "file";

    private HttpPostRequestDecoder decoder;
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(true);

    private boolean readingChunks;

    private static final int THUMB_MAX_WIDTH = 100;
    private static final int THUMB_MAX_HEIGHT = 100;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // get the URL

        URI uri = new URI(request.getUri());
        String uriStr = uri.getPath();

        System.out.println(request.getMethod() + " request received");

        if (request.getMethod() == HttpMethod.GET) {
            serveFile(ctx, request); // user requested a file, serve it
        } else if (request.getMethod() == HttpMethod.POST) {
            uploadFile(ctx, request); // user requested to upload file, handle request
        } else {
            // unknown request, send error message
            System.out.println(request.getMethod() + " request received, sending 405");
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
        }

    }

    private void serveFile(ChannelHandlerContext ctx, FullHttpRequest request) {

        // decode the query string
        QueryStringDecoder decoderQuery = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> uriAttributes = decoderQuery.parameters();

        // get the requested file name
        String fileName = "";
        try {
            fileName = uriAttributes.get(FILE_QUERY_PARAM).get(0);
        } catch (Exception e) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, FILE_QUERY_PARAM + " query param not found");
            return;
        }

        // start serving the requested file
        sendFile(ctx, fileName, request);
    }

    /**
     * This method reads the requested file from disk and sends it as response.
     * It also sets proper content-type of the response header
     *
     * @param fileName name of the requested file
     */
    private void sendFile(ChannelHandlerContext ctx, String fileName, FullHttpRequest request) {
        File file = new File(BASE_PATH + fileName);
        if (file.isDirectory() || file.isHidden() || !file.exists()) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        if (!file.isFile()) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        RandomAccessFile raf;

        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException fnfe) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        long fileLength = 0;
        try {
            fileLength = raf.length();
        } catch (IOException ex) {
            Logger.getLogger(HttpStaticFileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        setContentLength(response, fileLength);
        setContentTypeHeader(response, file);

        //setDateAndCacheHeaders(response, file);
        if (isKeepAlive(request)) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        // Write the initial line and the header.
        ctx.write(response);

        // Write the content.
        ChannelFuture sendFileFuture;
        DefaultFileRegion defaultRegion = new DefaultFileRegion(raf.getChannel(), 0, fileLength);
        sendFileFuture = ctx.write(defaultRegion);

        // Write the end marker
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        // Decide whether to close the connection or not.
        if (!isKeepAlive(request)) {
            // Close the connection when the whole content is written out.
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * This will set the content types of files. If you want to support any
     * files add the content type and corresponding file extension here.
     *
     * @param response
     * @param file
     */
    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        mimeTypesMap.addMimeTypes("image png tif jpg jpeg bmp");
        mimeTypesMap.addMimeTypes("text/plain txt");
        mimeTypesMap.addMimeTypes("application/pdf pdf");

        String mimeType = mimeTypesMap.getContentType(file);

        response.headers().set(CONTENT_TYPE, mimeType);
    }

    private void uploadFile(ChannelHandlerContext ctx, FullHttpRequest request) {

        // test comment
        try {
            decoder = new HttpPostRequestDecoder(factory, request);
            //System.out.println("decoder created");
        } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
            e1.printStackTrace();
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Failed to decode file data");
            return;
        }

        readingChunks = HttpHeaders.isTransferEncodingChunked(request);

        if (decoder != null) {
            if (request instanceof HttpContent) {

                // New chunk is received
                HttpContent chunk = (HttpContent) request;
                try {
                    decoder.offer(chunk);
                } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
                    e1.printStackTrace();
                    sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Failed to decode file data");
                    return;
                }

                readHttpDataChunkByChunk(ctx);
                // example of reading only if at the end
                if (chunk instanceof LastHttpContent) {
                    readingChunks = false;
                    reset();
                }
            } else {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Not a http request");
            }
        } else {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Failed to decode file data");
        }

    }

    private void sendOptionsRequestResponse(ChannelHandlerContext ctx) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendResponse(ChannelHandlerContext ctx, String responseString,
                              String contentType, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(responseString, CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, contentType);
        response.headers().add("Access-Control-Allow-Origin", "*");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendUploadedFileName(JSONObject fileName, ChannelHandlerContext ctx) {
        JSONObject jsonObj = new JSONObject();

        String msg = "Unexpected error occurred";
        String contentType = "application/json; charset=UTF-8";
        HttpResponseStatus status = HttpResponseStatus.OK;

        if (fileName != null) {
            msg = fileName.toString();
        } else {
            Logger.getLogger(HttpStaticFileServerHandler.class.getName()).log(
                    Level.SEVERE, "uploaded file names are blank");
            status = HttpResponseStatus.BAD_REQUEST;
            contentType = "text/plain; charset=UTF-8";
        }

        sendResponse(ctx, msg, contentType, status);

    }

    private void reset() {
        //request = null;

        // destroy the decoder to release all resources
        decoder.destroy();
        decoder = null;
    }

    /**
     * Example of reading request by chunk and getting values from chunk to
     * chunk
     */
    private void readHttpDataChunkByChunk(ChannelHandlerContext ctx) {
        //decoder.isMultipart();
        if (decoder.isMultipart()) {
            try {
                while (decoder.hasNext()) {
                    //System.out.println("decoder has next");
                    InterfaceHttpData data = decoder.next();
                    if (data != null) {
                        writeHttpData(data, ctx);
                        data.release();
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        } else {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Not a multipart request");
        }

        //System.out.println("decoder has no next");
    }

    private void writeHttpData(InterfaceHttpData data, ChannelHandlerContext ctx) {

        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
            FileUpload fileUpload = (FileUpload) data;

            if (fileUpload.isCompleted()) {
                JSONObject json = saveFileToDisk(fileUpload);
                sendUploadedFileName(json, ctx);
            } else {
                //responseContent.append("\tFile to be continued but should not!\r\n");
                sendError(ctx, HttpResponseStatus.BAD_REQUEST, "Unknown error occurred");
            }
        }
    }

    /**
     * generates and returns a unique string that'll be used to save an uploaded
     * file to disk
     *
     * @return generated unique string
     */
    private String getUniqueId() {
        UUID uniqueId = UUID.randomUUID();

        return uniqueId.toString();
    }

    /**
     * Saves the uploaded file to disk.
     *
     * @param fileUpload FileUpload object that'll be saved
     * @return name of the saved file. null if error occurred
     */
    private JSONObject saveFileToDisk(FileUpload fileUpload) {

        JSONObject responseJson = new JSONObject();

        String filePath = null; // full path of the new file to be saved
        String upoadedFileName = fileUpload.getFilename();

        // get the extension of the uploaded file
        String extension = "";
        int i = upoadedFileName.lastIndexOf('.');
        if (i > 0) {
            // get extension including the "."
            extension = upoadedFileName.substring(i);
        }

        String uniqueBaseName = getUniqueId();
        String fileName = uniqueBaseName + extension;

        try {
            filePath = BASE_PATH + fileName;

            fileUpload.renameTo(new File(filePath)); // enable to move into another
            UnZip.unzip(filePath,filePath.substring(0,filePath.lastIndexOf(".")));
            responseJson.put("file", fileName);
            if (isImageExtension(extension)) {
                String thumbname = createThumbnail(filePath, uniqueBaseName, extension);
                responseJson.put("thumb", thumbname);
            }
        } catch (IOException ex) {
            responseJson = null;
        } catch (JSONException ex) {
            Logger.getLogger(HttpStaticFileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            responseJson = null;
        }

        return responseJson;
    }

    /**
     * Creates a thumbnail of an image file
     *
     * @param fileFullPath full path of the source image
     * @param fileNameBase Base name of the file i.e without extension
     * @param extension extension of the file
     */
    private String createThumbnail(String fileFullPath, String fileNameBase, String extension) {
        String thumbImgName = fileNameBase + "_thumb" + extension; // thumbnail image base name
        String thumbImageFullPath = BASE_PATH + thumbImgName; // all thumbs are jpg files

        try {
            Thumbnails.of(new File(fileFullPath))
                    .size(100, 100)
                    .toFile(new File(thumbImageFullPath));
        } catch (IOException ex) {
            Logger.getLogger(HttpStaticFileServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            thumbImgName = "";
        }

//        Logger.getLogger(HttpStaticFileServerHandler.class.getName()).log(Level.SEVERE, null,
//                "Creating thumbnail of image " + fileFullPath);
//
//
//
//        //Scalr.resize(null, THUMB_MAX_WIDTH, null);
//        try {
//            BufferedImage img = ImageIO.read(new File(fileFullPath));
//            BufferedImage scaledImg = Scalr.resize(img, THUMB_MAX_WIDTH);
//
//            //BufferedImage scaledImg = Scalr.resize(img, Mode.AUTOMATIC, 640, 480);
//            File destFile = new File(thumbImageFullPath);
//
//            ImageIO.write(scaledImg, "jpg", destFile);
//        } catch (ImagingOpException | IOException | IllegalArgumentException e) {
//            Logger.getLogger(HttpStaticFileServerHandler.class.getName()).log(Level.SEVERE, null, e);
//            e.printStackTrace();
//            System.out.println(e.toString());
//            thumbImgName = "";
//        }

        return thumbImgName;

    }

    private static boolean isImageExtension(String extension) {
        boolean isImageFile = false;
        String extensionInLowerCase = extension.toLowerCase();

        isImageFile |= extensionInLowerCase.equals(".jpg");
        isImageFile |= extensionInLowerCase.equals(".png");
        isImageFile |= extensionInLowerCase.equals(".jpeg");
        isImageFile |= extensionInLowerCase.equals(".gif");

        return isImageFile;

    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        sendError(ctx, status, "Failure: " + status.toString() + "\r\n");
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {

        BufferedImage resizedImage = new BufferedImage(THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, THUMB_MAX_WIDTH, THUMB_MAX_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }

}
