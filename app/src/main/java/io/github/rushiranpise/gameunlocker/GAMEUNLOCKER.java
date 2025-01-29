package io.github.rushiranpise.gameunlocker;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.Arrays;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressLint("DiscouragedPrivateApi")
@SuppressWarnings("ConstantConditions")
public class GAMEUNLOCKER implements IXposedHookLoadPackage {
    private static final String TAG = GAMEUNLOCKER.class.getSimpleName();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        String packageName = loadPackageParam.packageName;

        // Check each device's package list and apply appropriate spoofing
        if (Arrays.asList(List.ROG6_PACKAGES).contains(packageName)) {
            applyDeviceProps(List.ROG6);
            XposedBridge.log("Spoofed " + packageName + " as Asus ROG 6");
        }
        else if (Arrays.asList(List.XPERIA5_PACKAGES).contains(packageName)) {
            applyDeviceProps(List.XPERIA5);
            XposedBridge.log("Spoofed " + packageName + " as Sony Xperia 5");
        }
        else if (Arrays.asList(List.OP8PRO_PACKAGES).contains(packageName)) {
            applyDeviceProps(List.OP8PRO);
            XposedBridge.log("Spoofed " + packageName + " as OnePlus 8 Pro");
        }
        else if (Arrays.asList(List.OP9PRO_PACKAGES).contains(packageName)) {
            applyDeviceProps(List.OP9PRO);
            XposedBridge.log("Spoofed " + packageName + " as OnePlus 9 Pro");
        }
        else if (Arrays.asList(List.MI11TPRO_PACKAGES).contains(packageName)) {
            applyDeviceProps(List.MI11TPRO);
            XposedBridge.log("Spoofed " + packageName + " as Xiaomi Mi 11T Pro");
        }
        else if (Arrays.asList(List.MI13PRO_PACKAGES).contains(packageName)) {
            applyDeviceProps(List.MI13PRO);
            XposedBridge.log("Spoofed " + packageName + " as Xiaomi Mi 13 Pro");
        }
        else if (Arrays.asList(List.POCOF5_PACKAGES).contains(packageName)) {
            applyDeviceProps(List.POCOF5);
            XposedBridge.log("Spoofed " + packageName + " as POCO F5");
        }
        else if (Arrays.asList(List.BS4_PACKAGES).contains(packageName)) {
            applyDeviceProps(List.BS4);
            XposedBridge.log("Spoofed " + packageName + " as Black Shark 4");
        }
        else if (Arrays.asList(List.IQOO11PRO_PACKAGES).contains(packageName)) {
            applyDeviceProps(List.IQOO11PRO);
            XposedBridge.log("Spoofed " + packageName + " as iQOO 11 Pro");
        }
    }

    private void applyDeviceProps(List.DeviceProps props) {
        // Base device properties
        setPropValue("MANUFACTURER", props.manufacturer);
        setPropValue("MODEL", props.model);
        setPropValue("BRAND", props.brand);
        setPropValue("DEVICE", props.device);
        
        // Extended properties for different partitions
        String[] partitions = {"", "odm.", "product.", "system.", "system_ext.", "vendor."};
        for (String partition : partitions) {
            String prefix = "ro.product." + partition;
            setPropValue(prefix + "brand", props.brand);
            setPropValue(prefix + "device", props.device);
            setPropValue(prefix + "manufacturer", props.manufacturer);
            setPropValue(prefix + "model", props.model);
            setPropValue(prefix + "marketname", props.marketname);
        }
        
        // SoC properties
        setPropValue("ro.soc.manufacturer", props.socManufacturer);
        setPropValue("ro.soc.model", props.socModel);
        
        // FPS properties
        setPropValue("sys.fps_unlock_allowed", String.valueOf(props.defaultFps));
        setPropValue("ro.vendor.display.default_fps", String.valueOf(props.defaultFps));
        setPropValue("ro.fps.capsmin", String.valueOf(props.defaultFps));
        setPropValue("ro.fps.capsmax", String.valueOf(props.defaultFps));
        
        // GPU and CPU properties
        setPropValue("ro.hardware.gpu", props.gpuModel);
        setPropValue("ro.hardware.chipname", props.cpuModel);
        
        // FPS Stabilizer
        setPropValue("debug.sf.showupdates", "0");
        setPropValue("debug.sf.showcpu", "0");
        setPropValue("debug.sf.showbackground", "0");
        setPropValue("debug.sf.showfps", "0");
        
        // System Properties
        setPropValue("cpu.fps", "auto");
        setPropValue("gpu.fps", "auto");
        setPropValue("ro.fps_enable", "0");
    }

    private static void setPropValue(String key, Object value) {
        try {
            Log.d(TAG, "Defining prop " + key + " to " + value.toString());
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            XposedBridge.log("Failed to set prop: " + key + "\n" + Log.getStackTraceString(e));
        }
    }
}