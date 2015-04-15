/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jgap.util.ICloneable;

/**
 *
 * @author Andrei
 */
public class Node implements ICloneable {

    private String id;
    private String name;
    
    private final int HASH = 7;
    private final int HASH_SEED = 71;

    public Node(String id) {
	this.id = id;
	this.name = id;
    }

    public Node(String id, String name) {
	this.id = id;
	this.name = name;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Node ")
		.append(this.hashCode()).append(" [ID: ")
		.append(this.id)
		.append(", Name: ")
		.append(this.name)
		.append("]");

	return builder.toString();
    }
    
    public JsonObject toJson() {
	JsonObject node = new JsonObject();
	node.addProperty("id", this.id);
	node.addProperty("name", this.name);
	
	JsonObject data = new JsonObject();
	node.add("data", data);
	
	JsonArray adjacencies = new JsonArray();	
	node.add("adjacencies", adjacencies);
	
	return node;
    }

    @Override
    public int hashCode() {
	int hash = HASH;
	hash = HASH_SEED * hash + (this.id != null ? this.id.hashCode() : 0);
	hash = HASH_SEED * hash + (this.name != null ? this.name.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}

	if (getClass() != obj.getClass()) {
	    return false;
	}

	final Node other = (Node) obj;
	if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
	    return false;
	}

	return !((this.name == null) ? (other.name != null) : !this.name.equals(other.name));
    }

    @Override
    public Node clone() {
	return new Node(this.getId(), this.getName());
    }
}
