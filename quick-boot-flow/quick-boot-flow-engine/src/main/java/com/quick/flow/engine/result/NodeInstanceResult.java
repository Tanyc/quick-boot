package com.quick.flow.engine.result;

import com.quick.flow.engine.bo.NodeInstance;
import com.google.common.base.MoreObjects;

public class NodeInstanceResult extends CommonResult {
    private NodeInstance nodeInstance;

    public NodeInstance getNodeInstance() {
        return nodeInstance;
    }

    public void setNodeInstance(NodeInstance nodeInstance) {
        this.nodeInstance = nodeInstance;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("errCode", getErrCode())
                .add("errMsg", getErrMsg())
                .add("nodeInstance", nodeInstance)
                .toString();
    }
}
