package me.prism3.logger.commands.getting;

public class Zabor2 {

    private String condition = "";
    private String tableName;

    public Zabor2(String tableName) { this.tableName = tableName; }

    public Zabor2(String tableName, String condition) {
        this.tableName = tableName;
        this.condition = condition;
    }

    public String getCondition() { return condition; }

    public void setCondition(String condition) { this.condition = condition; }

    public String getTableName() { return tableName; }

    public void setTableName(String tableName) { this.tableName = tableName; }
}
