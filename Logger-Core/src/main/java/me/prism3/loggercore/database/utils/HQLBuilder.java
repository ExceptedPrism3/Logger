package me.prism3.loggercore.database.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class HQLBuilder {

    private final String entityName;
    private final String entityAlias;
    private String condition;

    public HQLBuilder(String entityName, String entityAlias) {
        this.entityName = entityName;
        this.entityAlias = entityAlias;
    }

    public HQLBuilder addWhere(String field, String fieldValue, ConditionOperator conditionOperator, LogicalOperator logicalOperator) {

        if (condition == null) {
            where(field, fieldValue, conditionOperator);
            return this;
        }

        this.condition += " " + logicalOperator.name() + "" + this.entityAlias + "." + field + "" + conditionOperator.getCondition() + " '" + fieldValue + "'";
        return this;
    }

    public HQLBuilder where(String field, String fieldValue, ConditionOperator conditionOperator) {
        this.condition = "WHERE " + entityAlias + "." + field + " " + conditionOperator.getCondition() + " '" + fieldValue + "'";
        return this;
    }

    public HQLBuilder addDateCondition(String from) {

        if (this.condition == null) {
            this.condition = " WHERE " + entityAlias + "." + "date >= '" + from + "'";
            return this;
        }

        this.condition += " AND " + entityAlias + "." + "date >= '" + from + "'";
        return this;
    }

    public List<?> getResultsPaginated(Integer page, Integer limit) {

        Transaction tx = null;

        try {
            final Session session = HibernateUtils.getSession();

            tx = session.beginTransaction();

            final Query query = session.createQuery(this.toHql());

            query.setReadOnly(true);
            query.setCacheable(true);
            query.setFirstResult((page - 1) * limit);
            query.setMaxResults(limit);
            return query.list();
        } finally {
            if (tx != null)
                tx.commit();
        }
    }

    public String toHql() {
        return "SELECT " + this.entityAlias + " FROM " + entityName + " " + entityAlias + " " + (condition != null ?
                condition : "") + " ORDER " +
                "BY " + this.entityAlias + "." + "date DESC";
    }

    public Long getTotalCount() {

        Transaction tx = null;

        try {
            final Session session = HibernateUtils.getSession();
            tx = session.beginTransaction();
            final Query query = session.createQuery(
                    "SELECT count(*) FROM " + entityName + " " + entityAlias + " " + (condition != null ? condition :
                            ""));
            query.setReadOnly(true);
            query.setCacheable(true);
            return (Long) query.uniqueResult();
        } finally {
            if (tx != null)
                tx.commit();
        }
    }


    public enum LogicalOperator {
        AND,
        OR,
        NOT
    }

    public enum ConditionOperator {

        LIKE("LIKE"),
        EQUALS("=");
        private final String condition;

        ConditionOperator(String condition) {
            this.condition = condition;
        }

        public String getCondition() {
            return this.condition;
        }
    }
}
