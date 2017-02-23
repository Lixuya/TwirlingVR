package com.twirling.libtwirling.database;

import android.content.Context;

import com.twirling.libtwirling.Constants;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Target: Realm init
 */
public class RealmHelper {
	protected Realm realm = null;

	protected RealmHelper(Context context) {
		Realm.init(context);
		RealmConfiguration realmConfig = new RealmConfiguration.Builder()
				.deleteRealmIfMigrationNeeded()
//                .schemaVersion(2) // Must be bumped when the schema changes
//                .migration(new MyRealmMigration()) // Migration to run instead of throwing an exception
				.build();
		Realm.setDefaultConfiguration(realmConfig);
		realm = Realm.getDefaultInstance();
	}

	public void exportDatabase() {
		File exportRealmFile = new File(Constants.PATH_DOWNLOAD, "export.realm");
		if (exportRealmFile.exists()) {
			exportRealmFile.delete();
		}
		realm.writeCopyTo(exportRealmFile);
		realm.close();
	}
}
