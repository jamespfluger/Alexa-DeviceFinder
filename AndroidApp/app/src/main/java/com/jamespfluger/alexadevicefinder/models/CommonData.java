package com.jamespfluger.alexadevicefinder.models;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public final class CommonData {
    public static HashSet<String> names = new HashSet<>();

    public void initData(AssetManager assets) {
        new AsyncFileReader().execute(assets);
    }

    // Amazon won't tell us what first names are valid, so we need to check locally
    private static class AsyncFileReader extends AsyncTask<AssetManager, Void, HashSet<String>> {
        @Override
        protected HashSet<String> doInBackground(AssetManager... assets) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(assets[0].open("names.txt"), StandardCharsets.UTF_8))) {
                String name;
                while ((name = reader.readLine()) != null) {
                    names.add(name);
                }
            } catch (IOException e) {
            }

            return names;
        }
    }
}
