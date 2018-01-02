package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.HomeBtn;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;


public class BrandDataSource extends RxDataSource<List<HomeBtn>> implements IDataCacheLoader<List<HomeBtn>> {
//    private RealmConfiguration realmConfig;

    public BrandDataSource(Context context) {
        super(context);

//        realmConfig = new RealmConfiguration.Builder().name("BrandList").schemaVersion(BuildConfig.VERSION_CODE).migration(TaoKe.getRealmMigration()).build();
    }

    @Override
    public Observable<List<HomeBtn>> refresh() throws Exception {
        return TaoKeApi.getBrandList();
//        return TaoKeApi.getBrandList().map(homeBtns -> {
//            Realm realm = Realm.getInstance(realmConfig);
//            realm.executeTransactionAsync(bgRealm -> {
//                bgRealm.delete(HomeBtn.class);
//                bgRealm.insertOrUpdate(homeBtns);
//            });
//            realm.close();
//            return homeBtns;
//        });
    }

    @Override
    public Observable<List<HomeBtn>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<HomeBtn> loadCache(boolean isEmpty) {
        return null;
//        Realm realm = Realm.getInstance(realmConfig);
//        RealmResults<HomeBtn> results = realm.where(HomeBtn.class).findAll();
//        List<HomeBtn> data = HomeBtn.from(results);
//        realm.close();
//        return data;
    }
}