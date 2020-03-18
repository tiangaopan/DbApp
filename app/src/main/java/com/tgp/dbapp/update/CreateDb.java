package com.tgp.dbapp.update;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class CreateDb {
    /**
     * 数据库表名
     */
    private String name;

    private List<String> sqlCreates;

    public CreateDb(Element element) {
        name = element.getAttribute("name");
        sqlCreates = new ArrayList<>();
        NodeList nodeList = element.getElementsByTagName("sql_createTable");
        for (int i = 0; i < nodeList.getLength(); i++) {
            String textContent = nodeList.item(i).getTextContent();
            sqlCreates.add(textContent);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSqlCreates() {
        return sqlCreates;
    }

    public void setSqlCreates(List<String> sqlCreates) {
        this.sqlCreates = sqlCreates;
    }
}
