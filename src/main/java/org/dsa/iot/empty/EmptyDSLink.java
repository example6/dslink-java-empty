package org.dsa.iot.empty;

import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.DSLinkFactory;
import org.dsa.iot.dslink.DSLinkHandler;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeManager;
import org.dsa.iot.dslink.node.Permission;
import org.dsa.iot.dslink.node.actions.Action;
import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.actions.Parameter;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.handler.Handler;

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
		
		// Get node manager
		NodeManager manager = link.getNodeManager();
		
		// Get this DSLink's root node
		Node superRoot = manager.getNode("/").getNode();
		
		// Create a child node
		Node child1 = superRoot.createChild("cool name").build();
		
		// Create a value under Child1
		final Node myValue = child1.createChild("Value1")
			.setValueType(ValueType.NUMBER)
			.setValue(new Value(1000))
			.setSerializable(false)
			.build();
		
		// Create an action that adds to Value1
		Action addAction = new Action(Permission.READ, new Handler<ActionResult>() {
			@Override
			public void handle(ActionResult event) {
				int amountToAdd = event.getParameter("AmountToAdd").getNumber().intValue();
				int currentValue = myValue.getValue().getNumber().intValue();
				int result = amountToAdd + currentValue;
				myValue.setValue(new Value(result));
			}
		});
		addAction.addParameter(new Parameter("AmountToAdd", ValueType.NUMBER));
		
		// Add an action to a node
		Node child1Action1 = myValue.createChild("AddToValue")
			.setAction(addAction)
			.setSerializable(false)
			.build();
    }

    public static void main(String[] args) {
        DSLinkFactory.start(args, new EmptyDSLink());
    }
}
