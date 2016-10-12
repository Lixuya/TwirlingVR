package com.twirling.SDTL.data;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by xieqi on 2016/10/12.
 */

public class MyRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            schema.get("VideoItem")
                    .addField("AppIOSOffline", String.class);
//                    .addRealmObjectField("AppIOSOffline", schema.get("VideoItem"));
            oldVersion++;
        }
    }
}
