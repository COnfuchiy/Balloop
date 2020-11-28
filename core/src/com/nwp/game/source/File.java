package com.nwp.game.source;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class File {
    public enum WritingResult {
        Success,
        Error
    }
    public static boolean isExtAvailable = Gdx.files.isExternalStorageAvailable();
    public static boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();
    public static WritingResult WriteLine(String fileName, String line) {
        try {
            FileHandle file = Gdx.files.local(fileName);
            file.writeString(line, false);
            return WritingResult.Success;
        } catch (Exception e) {
            return WritingResult.Error;
        }
    }
    public static String ReadAll(String fileName) {
        try {
            FileHandle file = Gdx.files.local(fileName);
            return file.readString();
        } catch (Exception e) {
            return "";
        }
    }
}
