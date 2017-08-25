package org.dsa.iot.demo;

import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.DSLinkFactory;
import org.dsa.iot.dslink.DSLinkHandler;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeManager;
import org.dsa.iot.dslink.node.Permission;
import org.dsa.iot.dslink.node.actions.Action;
import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.actions.Parameter;
import org.dsa.iot.dslink.node.actions.ResultType;
import org.dsa.iot.dslink.node.actions.table.Row;
import org.dsa.iot.dslink.node.value.Value;
import org.dsa.iot.dslink.node.value.ValueType;
import org.dsa.iot.dslink.util.handler.Handler;

public class EmptyDSLink extends DSLinkHandler {
    private Node child1;
    private Node child1Value;
    private Node child1Action1;
    private Node child1Action2;

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
        this.child1 = superRoot.createChild("Child1", false).build();

        // Create a value under Child1
        this.child1Value = child1.createChild("Value1", false)
                .setValueType(ValueType.NUMBER)
                .setValue(new Value(1000))
                .setSerializable(false)
                .build();

        // Create an action on Child1 that adds to Value1
        this.child1Action1 = child1.createChild("AddToValue", false)
                .setAction(this.addToValue())
                .setSerializable(false)
                .build();

        // Create an action on Child1 that concatenates two strings
        this.child1Action1 = child1.createChild("ConcatenateTwoStrings", false)
                .setAction(this.concatenateStrings())
                .setSerializable(false)
                .build();
                
    }

    // Add the given number to Value1
    private Action addToValue() {
        final Node myValue = this.child1Value;

        Action action = new Action(Permission.READ, new Handler<ActionResult>() {

            @Override
            public void handle(ActionResult event) {
                int amountToAdd = event.getParameter("AmountToAdd").getNumber().intValue();
                int currentValue = myValue.getValue().getNumber().intValue();
                int result = amountToAdd + currentValue;

                myValue.setValue(new Value(result));
            }
        });

        action.addParameter(new Parameter("AmountToAdd", ValueType.NUMBER));

        return action;
    }

    private Action concatenateStrings() {
        Action action = new Action(Permission.READ, new Handler<ActionResult>() {

            @Override
            public void handle(ActionResult event) {
                String string1 = event.getParameter("String1").getString();
                String string2 = event.getParameter("String2").getString();
                String newString = string1 + string2;

                event.getTable().addRow(Row.make(new Value(newString)));
            }
        });

        action.addParameter(new Parameter("String1", ValueType.STRING));
        action.addParameter(new Parameter("String2", ValueType.STRING));
        action.addResult(new Parameter("Result1", ValueType.STRING));
        action.setResultType(ResultType.VALUES);

        return action;
    }

    public static void main(String[] args) {
        DSLinkFactory.start(args, new EmptyDSLink());
    }
}
