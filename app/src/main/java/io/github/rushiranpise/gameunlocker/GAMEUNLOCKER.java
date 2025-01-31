package io.github.rushiranpise.gameunlocker;

import android.annotation.SuppressLint;
import android.util.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;

@SuppressLint("DiscouragedPrivateApi")
public class GAMEUNLOCKER implements IXposedHookLoadPackage {
    private static final String TAG = GAMEUNLOCKER.class.getSimpleName();
    private final Map<String, String> spoofedProps = new HashMap<>();
    private String currentPackage = "";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        String packageName = lpparam.packageName;
        currentPackage = packageName;

        // Hook SystemProperties.get() to spoof device properties
        hookSystemProperties(lpparam);

        // Hook Activity lifecycle to apply spoofing when app is opened
        hookActivityLifecycle(lpparam);
    }

    private void hookSystemProperties(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedHelpers.findAndHookMethod(
                "android.os.SystemProperties",
                lpparam.classLoader,
                "get",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        String key = (String) param.args[0];
                        if (shouldSpoof(currentPackage) && spoofedProps.containsKey(key)) {
                            // Spoof the property value
                            param.setResult(spoofedProps.get(key));
                            Log.d(TAG, "Spoofed " + key + " to " + spoofedProps.get(key));
                        }
                    }
                }
            );
        } catch (Throwable t) {
            XposedBridge.log("Failed to hook SystemProperties.get(): " + t.getMessage());
        }
    }

    private void hookActivityLifecycle(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader,
                "onResume", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        if (shouldSpoof(currentPackage)) {
                            updateDeviceProps();
                        }
                    }
                });

        XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader,
                "onPause", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        if (shouldSpoof(currentPackage)) {
                            clearSpoofedProps();
                        }
                    }
                });
    }

    private void updateDeviceProps() {
        if (Arrays.asList(List.ROG6_PACKAGES).contains(currentPackage)) {
            applyDeviceProps(List.ROG6, "ROG Phone 6");
        } else if (Arrays.asList(List.XPERIA5_PACKAGES).contains(currentPackage)) {
            applyDeviceProps(List.XPERIA5, "Xperia 5 IV");
        } else if (Arrays.asList(List.OP8PRO_PACKAGES).contains(currentPackage)) {
            applyDeviceProps(List.OP8PRO, "OnePlus 8 Pro");
        } else if (Arrays.asList(List.OP9PRO_PACKAGES).contains(currentPackage)) {
            applyDeviceProps(List.OP9PRO, "OnePlus 9 Pro");
        } else if (Arrays.asList(List.MI11TPRO_PACKAGES).contains(currentPackage)) {
            applyDeviceProps(List.MI11TPRO, "Mi 11T Pro");
        } else if (Arrays.asList(List.MI13PRO_PACKAGES).contains(currentPackage)) {
            applyDeviceProps(List.MI13PRO, "Xiaomi 13 Pro");
        } else if (Arrays.asList(List.POCOF5_PACKAGES).contains(currentPackage)) {
            applyDeviceProps(List.POCOF5, "POCO F5");
        } else if (Arrays.asList(List.BS4_PACKAGES).contains(currentPackage)) {
            applyDeviceProps(List.BS4, "Black Shark 4");
        } else if (Arrays.asList(List.IQOO11PRO_PACKAGES).contains(currentPackage)) {
            applyDeviceProps(List.IQOO11PRO, "iQOO 11 Pro");
        }
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

    private void applyDeviceProps(List.DeviceProps props, String deviceName) {
        try {
            // Base properties
            spoofedProps.put("ro.product.manufacturer", props.manufacturer);
            spoofedProps.put("ro.product.model", props.model);
            spoofedProps.put("ro.product.brand", props.brand);
            spoofedProps.put("ro.product.device", props.device);
            spoofedProps.put("ro.product.name", props.device);
            spoofedProps.put("ro.product.marketname", props.marketname);

            // Extended properties for different partitions
            String[] partitions = {"", "odm.", "product.", "system.", "system_ext.", "vendor."};
            for (String partition : partitions) {
                String prefix = "ro.product." + partition;
                spoofedProps.put(prefix + "brand", props.brand);
                spoofedProps.put(prefix + "device", props.device);
                spoofedProps.put(prefix + "manufacturer", props.manufacturer);
                spoofedProps.put(prefix + "model", props.model);
                spoofedProps.put(prefix + "name", props.device);
                spoofedProps.put(prefix + "marketname", props.marketname);
            }

            // SoC and hardware properties
            spoofedProps.put("ro.soc.manufacturer", props.socManufacturer);
            spoofedProps.put("ro.soc.model", props.socModel);
            spoofedProps.put("ro.hardware.gpu", props.gpuModel);
            spoofedProps.put("ro.hardware.chipname", props.cpuModel);

            // FPS related properties
            spoofedProps.put("sys.fps_unlock_allowed", String.valueOf(props.defaultFps));
            spoofedProps.put("ro.vendor.display.default_fps", String.valueOf(props.defaultFps));
            spoofedProps.put("ro.fps.capsmin", String.valueOf(props.defaultFps));
            spoofedProps.put("ro.fps.capsmax", String.valueOf(props.defaultFps));

            XposedBridge.log("Successfully spoofed as " + deviceName);
        } catch (Exception e) {
            XposedBridge.log("Error applying device props: " + e.getMessage());
        }
    }

    private void clearSpoofedProps() {
        spoofedProps.clear();
        XposedBridge.log("Cleared all spoofed properties");
    }
}