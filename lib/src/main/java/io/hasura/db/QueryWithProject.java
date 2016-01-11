package io.hasura.db;

import com.google.gson.*;

public abstract class QueryWithProject<Q, R> {
    protected JsonArray columns;

    public abstract Q fromColumns(JsonArray columns);

    public QueryWithProject() {
        this.columns = new JsonArray();
    }

    public Q columns(SelectField<R> f1) {
        this.columns.add(f1.toQCol());
        return fromColumns(this.columns);
    }

    public Q columns(SelectField<R> f1, SelectField<R> f2) {
        this.columns.add(f1.toQCol());
        this.columns.add(f2.toQCol());
        return fromColumns(this.columns);
    }

    public Q columns(SelectField<R> f1, SelectField<R> f2, SelectField<R> f3) {
        this.columns.add(f1.toQCol());
        this.columns.add(f2.toQCol());
        this.columns.add(f3.toQCol());
        return fromColumns(this.columns);
    }

}
