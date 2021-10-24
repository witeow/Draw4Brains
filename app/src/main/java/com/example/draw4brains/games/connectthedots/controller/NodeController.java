package com.example.draw4brains.games.connectthedots.controller;

import android.widget.ImageView;

import com.example.draw4brains.games.connectthedots.object.Node;

import java.util.ArrayList;

public class NodeController {


    private ArrayList<Node> nodeOrder;
    private ArrayList<ImageView> nodeImages;
    private String startNode = null;
    private String endNode= null;

    public NodeController() {
        nodeOrder = new ArrayList<>(); // Empty to be added
//        nodeImages = new ArrayList<>(); // Empty to be added
    }

    public void addNodeToManager(Node node, boolean isStartingNode, boolean isEndingNode){
        int indexToInsert = nodeOrder.size();
        this.nodeOrder.add(indexToInsert, node);
//        this.nodeImages.add(node.getNodeImage());
        if (isStartingNode) {
            // This is the first node:
            this.setStartNode(node.getName());
        }
        if (isEndingNode) {
            this.setEndNode(node.getName());
        }
    }

    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(String endNode) {
        this.endNode = endNode;
    }

    public String getStartNode() {
        return this.startNode;
    }

    public String getEndNode() {
        return this.endNode;
    }

    public ArrayList<Node> getNodeList() {
        return this.nodeOrder;
    }
}
