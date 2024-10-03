package org.jahia.community.maintenancepersite.filter;

import org.jahia.api.Constants;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.decorator.JCRSiteNode;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.filter.AbstractFilter;
import org.jahia.services.render.filter.RenderChain;
import org.jahia.services.render.filter.RenderFilter;
import org.kie.internal.io.ResourceChangeNotifier;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = RenderFilter.class)
public class MaintenancePerSiteFilter extends AbstractFilter {
	
	
	private static Logger log = LoggerFactory.getLogger(MaintenancePerSiteFilter.class);
	
    public static String MIXIN_MAINTENANCE = "jmix:maintenancePerSite";
	
	
	
    @Activate
    public void activate() {
        setPriority(3);
        setApplyOnEditMode(false);
        setSkipOnAjaxRequest(true);
        setApplyOnConfigurations("page");
        setApplyOnTemplateTypes("html,html-*");
    }
    @Override
    public String prepare(RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        return super.prepare(renderContext, resource, chain);
    }
    @Override
    public String execute(String previousOut, RenderContext renderContext, Resource resource, RenderChain chain) throws Exception {
        String output = super.execute(previousOut, renderContext, resource, chain);
        
        if (renderContext.getWorkspace().equals(Constants.LIVE_WORKSPACE) && resource.getNode().getResolveSite().isNodeType(MIXIN_MAINTENANCE)) {
        	JCRSiteNode site = resource.getNode().getResolveSite();
        	JCRNodeWrapper maintenancePage = (JCRNodeWrapper)site.getProperty("maintenancePage").getNode();
        	
        	if (maintenancePage != null && !resource.getNodePath().equals(maintenancePage.getPath())) {
        		log.debug("Maintenance Page " + maintenancePage.getPath() + " loaded");
        		renderContext.getResponse().sendRedirect(maintenancePage.getUrl());
        		return "";
        		
        		
        	} else {
        		log.warn("Mixin " + MIXIN_MAINTENANCE + " is set on Site, but no valid maintenance Page found.");
        	}
        }
        return output;
    }
 
}
