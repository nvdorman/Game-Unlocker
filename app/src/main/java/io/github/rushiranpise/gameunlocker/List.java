package io.github.rushiranpise.gameunlocker;

public class List {
    // Asus ROG Phone 6
    public static final String[] ROG6_PACKAGES = {
        "com.activision.callofduty.shooter",
        "com.activision.callofudty.warzone",
        "com.ea.gp.fifamobile",
        "com.gameloft.android.ANMP.GloftA9HM",
        "com.madfingergames.legends",
        "com.pearlabyss.blackdesertm",
        "com.pearlabyss.blackdesertm.gl",
        // Additional games for ROG 6
        "com.tencent.tmgp.pubgmhd",
        "com.pubg.imobile",
        "com.dts.freefiremax",
        "com.mobile.legends"
    };

    // Sony Xperia 5
    public static final String[] XPERIA5_PACKAGES = {
        "com.garena.game.codm",
        "com.tencent.tmgp.kr.codm",
        "com.vng.codmvn",
        "com.garena.game.kgvn",
        // Additional games for Xperia 5
        "com.tencent.ig",
        "com.pubg.krmobile",
        "com.vng.pubgmobile"
    };

    // OnePlus 8 Pro
    public static final String[] OP8PRO_PACKAGES = {
        "com.netease.lztgglobal",
        "com.pubg.imobile",
        "com.pubg.krmobile",
        "com.rekoo.pubgm",
        "com.riotgames.league.wildrift",
        "com.riotgames.league.wildrifttw",
        "com.riotgames.league.wildriftvn",
        "com.riotgames.league.teamfighttactics",
        "com.riotgames.league.teamfighttacticstw",
        "com.riotgames.league.teamfighttacticsvn",
        "com.tencent.ig",
        "com.tencent.tmgp.pubgmhd",
        "com.vng.pubgmobile",
        "vng.games.revelation.mobile",
        "com.ngame.allstar.eu",
        "com.mojang.minecraftpe",
        "com.YoStar.AetherGazer",
        "com.miHoYo.GenshinImpact",
        "com.garena.game.lmjx"
    };

    // OnePlus 9 Pro
    public static final String[] OP9PRO_PACKAGES = {
        "com.epicgames.fortnite",
        "com.epicgames.portal",
        "com.tencent.lolm",
        "jp.konami.pesam",
        // Additional games
        "com.ea.gp.fifamobile",
        "com.tencent.tmgp.sgame"
    };

    // Xiaomi Mi 11T Pro
    public static final String[] MI11TPRO_PACKAGES = {
        "com.ea.gp.apexlegendsmobilefps",
        "com.mobilelegends.mi",
        "com.levelinfinite.hotta.gp",
        "com.supercell.clashofclans",
        "com.vng.mlbbvn",
        // Additional games
        "com.garena.game.codm",
        "com.dts.freefireth"
    };

    // Xiaomi 13 Pro
    public static final String[] MI13PRO_PACKAGES = {
        "com.levelinfinite.sgameGlobal",
        "com.tencent.tmgp.sgame",
        // Additional games
        "com.mobile.legends",
        "com.miHoYo.GenshinImpact",
        "com.tencent.tmgp.cod"
    };

    // POCO F5
    public static final String[] POCOF5_PACKAGES = {
        "com.dts.freefiremax",
        "com.dts.freefireth",
        "com.mobile.legends",
        // Additional games
        "com.pubg.imobile",
        "com.tencent.ig",
        "com.supercell.clashofclans"
    };

    // Black Shark 4
    public static final String[] BS4_PACKAGES = {
        "com.proximabeta.mf.uamo",
        // Additional games
        "com.tencent.tmgp.pubgmhd",
        "com.pubg.imobile",
        "com.tencent.ig",
        "com.dts.freefiremax"
    };

    // iQOO 11 Pro
    public static final String[] IQOO11PRO_PACKAGES = {
        "com.tencent.KiHan",
        "com.tencent.tmgp.cf",
        "com.tencent.tmgp.cod",
        "com.tencent.tmgp.gnyx",
        // Additional games
        "com.mobile.legends",
        "com.garena.game.codm",
        "com.tencent.ig"
    };

    // Device Properties
    public static class DeviceProps {
        public final String manufacturer;
        public final String model;
        public final String brand;
        public final String device;

        public DeviceProps(String manufacturer, String model, String brand, String device) {
            this.manufacturer = manufacturer;
            this.model = model;
            this.brand = brand;
            this.device = device;
        }
    }

    // Device Properties Map
    public static final DeviceProps ROG6 = new DeviceProps("asus", "ASUS_AI2201", "asus", "AI2201");
    public static final DeviceProps XPERIA5 = new DeviceProps("Sony", "SO-52A", "Sony", "SO-52A");
    public static final DeviceProps OP8PRO = new DeviceProps("OnePlus", "IN2020", "OnePlus", "IN2020");
    public static final DeviceProps OP9PRO = new DeviceProps("OnePlus", "LE2123", "OnePlus", "LE2123");
    public static final DeviceProps MI11TPRO = new DeviceProps("Xiaomi", "2107113SI", "Xiaomi", "2107113SI");
    public static final DeviceProps MI13PRO = new DeviceProps("Xiaomi", "2210132C", "Xiaomi", "2210132C");
    public static final DeviceProps POCOF5 = new DeviceProps("Xiaomi", "23049PCD8G", "POCO", "23049PCD8G");
    public static final DeviceProps BS4 = new DeviceProps("blackshark", "SHARK PAR-H0", "blackshark", "SHARK PAR-H0");
    public static final DeviceProps IQOO11PRO = new DeviceProps("vivo", "V2243A", "iQOO", "V2243A");
}