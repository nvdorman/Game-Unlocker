package io.github.rushiranpise.gameunlocker;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;

@SuppressLint("DiscouragedPrivateApi")
public class GAMEUNLOCKER implements IXposedHookLoadPackage {
    private static final String TAG = GAMEUNLOCKER.class.getSimpleName();
    private final Map<String, String> originalProps = new ConcurrentHashMap<>();
    private String currentPackage = "";
    private boolean isFirstLaunch = true;
    private static final int COMMAND_TIMEOUT = 3000; // 3 seconds timeout

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        String packageName = lpparam.packageName;
        currentPackage = packageName;

        if (isFirstLaunch) {
            backupCurrentProps();
            isFirstLaunch = false;
        }

        // Hook Activity lifecycle
        hookActivityLifecycle(lpparam);
        
        // Initial check for spoofing
        checkAndApplySpoofing(packageName);
    }

    private void hookActivityLifecycle(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader,
                "onResume", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        checkAndApplySpoofing(currentPackage);
                    }
                });

        XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader,
                "onPause", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        if (shouldSpoof(currentPackage)) {
                            restoreOriginalProps();
                        }
                    }
                });
    }

    private boolean shouldSpoof(String packageName) {
        return Arrays.asList(List.ROG6_PACKAGES).contains(packageName) ||
               Arrays.asList(List.XPERIA5_PACKAGES).contains(packageName) ||
               Arrays.asList(List.OP8PRO_PACKAGES).contains(packageName) ||
               Arrays.asList(List.OP9PRO_PACKAGES).contains(packageName) ||
               Arrays.asList(List.MI11TPRO_PACKAGES).contains(packageName) ||
               Arrays.asList(List.MI13PRO_PACKAGES).contains(packageName) ||
               Arrays.asList(List.POCOF5_PACKAGES).contains(packageName) ||
               Arrays.asList(List.BS4_PACKAGES).contains(packageName) ||
               Arrays.asList(List.IQOO11PRO_PACKAGES).contains(packageName);
    }

    private void checkAndApplySpoofing(String packageName) {
        try {
            if (Arrays.asList(List.ROG6_PACKAGES).contains(packageName)) {
                applyDeviceProps(List.ROG6, "ROG Phone 6");
            } else if (Arrays.asList(List.XPERIA5_PACKAGES).contains(packageName)) {
                applyDeviceProps(List.XPERIA5, "Xperia 5 IV");
            } else if (Arrays.asList(List.OP8PRO_PACKAGES).contains(packageName)) {
                applyDeviceProps(List.OP8PRO, "OnePlus 8 Pro");
            } else if (Arrays.asList(List.OP9PRO_PACKAGES).contains(packageName)) {
                applyDeviceProps(List.OP9PRO, "OnePlus 9 Pro");
            } else if (Arrays.asList(List.MI11TPRO_PACKAGES).contains(packageName)) {
                applyDeviceProps(List.MI11TPRO, "Mi 11T Pro");
            } else if (Arrays.asList(List.MI13PRO_PACKAGES).contains(packageName)) {
                applyDeviceProps(List.MI13PRO, "Xiaomi 13 Pro");
            } else if (Arrays.asList(List.POCOF5_PACKAGES).contains(packageName)) {
                applyDeviceProps(List.POCOF5, "POCO F5");
            } else if (Arrays.asList(List.BS4_PACKAGES).contains(packageName)) {
                applyDeviceProps(List.BS4, "Black Shark 4");
            } else if (Arrays.asList(List.IQOO11PRO_PACKAGES).contains(packageName)) {
                applyDeviceProps(List.IQOO11PRO, "iQOO 11 Pro");
            }
        } catch (Exception e) {
            XposedBridge.log("Failed to apply device props: " + e.getMessage());
        }
    }

    private void applyDeviceProps(List.DeviceProps props, String deviceName) {
        try {
            // Base properties
            setPropValue("ro.product.manufacturer", props.manufacturer);
            setPropValue("ro.product.model", props.model);
            setPropValue("ro.product.brand", props.brand);
            setPropValue("ro.product.device", props.device);
            setPropValue("ro.product.name", props.device);
            setPropValue("ro.product.marketname", props.marketname);

            // Extended properties for different partitions
            String[] partitions = {"", "odm.", "product.", "system.", "system_ext.", "vendor."};
            for (String partition : partitions) {
                String prefix = "ro.product." + partition;
                setPropValue(prefix + "brand", props.brand);
                setPropValue(prefix + "device", props.device);
                setPropValue(prefix + "manufacturer", props.manufacturer);
                setPropValue(prefix + "model", props.model);
                setPropValue(prefix + "name", props.device);
                setPropValue(prefix + "marketname", props.marketname);
            }

            // SoC and hardware properties
            setPropValue("ro.soc.manufacturer", props.socManufacturer);
            setPropValue("ro.soc.model", props.socModel);
            setPropValue("ro.hardware.gpu", props.gpuModel);
            setPropValue("ro.hardware.chipname", props.cpuModel);

            // FPS related properties
            setPropValue("sys.fps_unlock_allowed", String.valueOf(props.defaultFps));
            setPropValue("ro.vendor.display.default_fps", String.valueOf(props.defaultFps));
            setPropValue("ro.fps.capsmin", String.valueOf(props.defaultFps));
            setPropValue("ro.fps.capsmax", String.valueOf(props.defaultFps));
            setPropValue("cpu.fps", "auto");
            setPropValue("gpu.fps", "auto");

            // Additional system properties
            setPropValue("debug.sf.showupdates", "0");
            setPropValue("debug.sf.showcpu", "0");
            setPropValue("debug.sf.showbackground", "0");
            setPropValue("debug.sf.showfps", "0");

            XposedBridge.log("Successfully spoofed as " + deviceName);
        } catch (Exception e) {
            XposedBridge.log("Error applying device props: " + e.getMessage());
        }
    }

    private void backupCurrentProps() {
        String[] propsToBackup = {
            "ro.product.manufacturer",
            "ro.product.model",
            "ro.product.brand",
            "ro.product.device",
            "ro.product.name",
            "ro.product.marketname",
            "ro.soc.manufacturer",
            "ro.soc.model",
            "ro.hardware.gpu",
            "ro.hardware.chipname",
            "sys.fps_unlock_allowed",
            "ro.vendor.display.default_fps",
            "ro.fps.capsmin",
            "ro.fps.capsmax"
        };

        String[] partitions = {"", "odm.", "product.", "system.", "system_ext.", "vendor."};
        
        try {
            for (String prop : propsToBackup) {
                String value = getProp(prop);
                if (value != null && !value.isEmpty()) {
                    originalProps.put(prop, value);
                }
                
                // Backup partition-specific properties
                for (String partition : partitions) {
                    if (prop.startsWith("ro.product.")) {
                        String partitionProp = "ro.product." + partition + 
                                              prop.substring("ro.product.".length());
                        String partitionValue = getProp(partitionProp);
                        if (partitionValue != null && !partitionValue.isEmpty()) {
                            originalProps.put(partitionProp, partitionValue);
                        }
                    }
                }
            }
            XposedBridge.log("Original device properties backed up successfully");
        } catch (Exception e) {
            XposedBridge.log("Failed to backup original props: " + e.getMessage());
        }
    }

    private void restoreOriginalProps() {
        try {
            for (Map.Entry<String, String> entry : originalProps.entrySet()) {
                setPropValue(entry.getKey(), entry.getValue());
            }
            XposedBridge.log("Original device properties restored");
        } catch (Exception e) {
            XposedBridge.log("Failed to restore original props: " + e.getMessage());
        }
    }

    private String getProp(String key) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "getprop " + key});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            if (process.waitFor(COMMAND_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                String line = reader.readLine();
                reader.close();
                return line;
            } else {
                process.destroy();
                throw new Exception("Command timed out");
            }
        } catch (Exception e) {
            XposedBridge.log("Failed to get prop " + key + ": " + e.getMessage());
            return "";
        }
    }

    private void setPropValue(String key, String value) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "setprop " + key + " " + value});
            if (!process.waitFor(COMMAND_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                process.destroy();
                throw new Exception("Command timed out");
            }
            Log.d(TAG, "Set " + key + " to " + value);
        } catch (Exception e) {
            XposedBridge.log("Failed to set prop " + key + ": " + e.getMessage());
        }
    }
}
