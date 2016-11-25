package cn.yunyichina.log.serviceCenter.reverseProxy.filter;

import cn.yunyichina.log.common.util.ThreadPool;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 10:33
 * @Description:
 */
//@Component
public class RouteFilter extends ZuulFilter {

    @Value("${task.route.collector-node}")
    private String collectorNodeRoute;

    @Value("${task.route.searcher-node}")
    private String searcherNodeRoute;

    @Autowired
    ThreadPool threadPool;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        String uri = req.getRequestURI();
        if (uri.startsWith(collectorNodeRoute)) {

        } else if (uri.startsWith(searcherNodeRoute)) {

        } else {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(404);
        }
        return null;
    }
}
