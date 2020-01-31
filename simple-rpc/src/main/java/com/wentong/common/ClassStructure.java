package com.wentong.common;

import java.util.StringJoiner;

public class ClassStructure {
    private String className;
    private String methodName;
    private String param;

    public ClassStructure(String className, String methodName, String param) {
        this.className = className;
        this.methodName = methodName;
        this.param = param;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ClassStructure.class.getSimpleName() + "[", "]")
                .add("className='" + className + "'")
                .add("methodName='" + methodName + "'")
                .add("param='" + param + "'")
                .toString();
    }
}
