package io.hasura.db.delete;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashSet;

import io.hasura.core.Call;
import io.hasura.db.Condition;
import io.hasura.db.DBException;
import io.hasura.db.DBResponseConverter;
import io.hasura.db.DBService;
import io.hasura.db.QueryWithReturning;
import io.hasura.db.Table;
import okhttp3.MediaType;

public class DeleteQuery<R> extends QueryWithReturning<DeleteQuery<R>, R> {
    private static Gson gson =
        new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    private JsonObject whereExp;
    private DBService db;
    private Table<R> table;

    public DeleteQuery<R> fromRetSet(HashSet<String> retSet) {
        this.retSet = retSet;
        return this;
    }

    public DeleteQuery(DBService db, Table<R> table) {
        super();
        this.whereExp = null;
        this.table = table;
        this.db = db;
    }

    public DeleteQuery<R> where(Condition<R> c) {
        this.whereExp = c.getBoolExp();
        return this;
    }

    public Call<DeleteResult<R>, DBException> build() {
        /* Create the query object */
        JsonObject query = new JsonObject();
        if (this.whereExp != null)
            query.add("where", this.whereExp);

        if (this.retSet.size() != 0) {
            JsonArray retArr = new JsonArray();
            for (String retCol : this.retSet)
                retArr.add(new JsonPrimitive(retCol));
            query.add("returning", retArr);
        }

        String opUrl = "/table/" + table.getTableName() + "/delete";
        return db.mkCall(opUrl, gson.toJson(query), new DBResponseConverter<DeleteResult<R>>(table.getDelResType()));
    }
}
