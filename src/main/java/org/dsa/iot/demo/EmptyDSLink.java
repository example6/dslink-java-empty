package org.dsa.iot.demo;

import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.DSLinkFactory;
import org.dsa.iot.dslink.DSLinkHandler;

public class EmptyDSLink extends DSLinkHandler {

    @Override
    public boolean isResponder() {
        return true;
    }

    @Override
    public void preInit() {
    }

	@Override
    public void onResponderConnected(DSLink link) {
                
    }

    public static void main(String[] args) {
        DSLinkFactory.start(args, new EmptyDSLink());
    }
}
