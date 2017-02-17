package cn.yunyichina.log.serviceCenter.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/11/24 10:33
 * @Description: not be used yet
 */
//@Component
public class RouteFilter extends ZuulFilter {

    private final String COLLECTOR_PREFIX = "collector-";
    private final String SEARCHER_PREFIX = "searcher-";

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
        if (uri.startsWith(COLLECTOR_PREFIX)) {

        } else if (uri.startsWith(SEARCHER_PREFIX)) {

        } else {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(404);
        }
        return null;
    }
}
