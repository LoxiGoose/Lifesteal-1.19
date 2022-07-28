package net.goose.lifesteal.Configurations;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public final ForgeConfigSpec.IntValue amountOfLives;
    public final ForgeConfigSpec.IntValue startingHeartDifference;
    public final ForgeConfigSpec.BooleanValue shouldAllMobsGiveHearts;
    public final ForgeConfigSpec.BooleanValue loseHeartsOnlyWhenKilledByPlayer;
    public final ForgeConfigSpec.IntValue amountOfHealthLostUponLoss;
    public final ForgeConfigSpec.IntValue maximumamountofheartsgainable;
    public final ForgeConfigSpec.IntValue minimumamountofheartscanhave;
    public final ForgeConfigSpec.BooleanValue disableLifesteal;
    public final ForgeConfigSpec.BooleanValue disableHeartCrystals;
    public final ForgeConfigSpec.BooleanValue disableHeartCores;
    public final ForgeConfigSpec.BooleanValue playersGainHeartsifKillednoHeart;
    public final ForgeConfigSpec.BooleanValue disableHeartLoss;
    public final ForgeConfigSpec.IntValue HeartCrystalAmountGain;
    public final ForgeConfigSpec.DoubleValue HeartCoreHeal;
    public final ForgeConfigSpec.BooleanValue disableEnchantments;

    //public final ForgeConfigSpec.BooleanValue bannedUponLosingAllHeartsOrLives;

    public Config(final ForgeConfigSpec.Builder builder) {
        builder.comment("It's recommended to edit the config BEFORE you make/play a world. While editing the config in an already generated world is supported, there may be visual bugs or just bugs in general.");
        builder.comment("This category holds general values that will mostly be customized by most.");
        builder.push("Starting Configurations");
        this.startingHeartDifference = buildInt(builder, "Starting HitPoint Difference:",  0, -19, Integer.MAX_VALUE, "This value modifies how many hearts you'll start at in a world. 2 would mean 1 extra heart, -2 would mean 1 less heart. If you have lives enabled, you'll gain a life when you get max hearts double your starting hearts. EX: If 3 hearts is your starting value, you'll gain a life if you get 3 more hearts. ");
        this.amountOfLives = buildInt(builder, "Starting Lives:",  -1, -1, Integer.MAX_VALUE,  "This introduces a new lives system where when a player loses all their max hearts, they will lose a life and their max hearts will reset. When they lose all their lives, they permanently die. You can gain a life if your max hearts are double your starting hearts. Setting this to -1 will disable this feature.");
        this.loseHeartsOnlyWhenKilledByPlayer = buildBoolean(builder, "Lose Hearts Only When Killed By a Player:",  false, "When this is false, you will lose hearts when killed by anything. Otherwise, you can only lose max hearts when killed by a player.");
        this.amountOfHealthLostUponLoss =  buildInt(builder, "Amount of HitPoints/Health Lost/Given Upon Death/Kill:",  2, 1, Integer.MAX_VALUE, "This values modifies the amount of hit points that should be lost when you die. The same also applies when you gain max health from lifestealing. 2 hit points = 1 health.");this.disableHeartLoss = buildBoolean(builder, "Disable Heart Loss:", false, "This value determines if a PLAYER should lose HEARTS AT ALL.");

        builder.pop();

        builder.comment("This category is the configuration for items and enchantments in this mod");
        builder.push("Items and Enchantments");
        this.HeartCrystalAmountGain = buildInt(builder, "Amount of HitPoints Heart Crystal Permanently Gives:", 2, 1, Integer.MAX_VALUE, "This is the amount of hit points a Heart Crystal should give when used. 2 HitPoints = 1 Heart, 3 = 1.5 Heart.");
        this.HeartCoreHeal = buildDouble(builder, "Percentage of max Health Heart Core Heals", 0.25, 0.01, 1, "The percentage of max health a heart core should heal when used.");
        this.disableHeartCrystals = buildBoolean(builder, "Disable Heart Crystals:", false, "If you just want the generic Lifesteal mod, you can disable this and nobody can gain hearts through Heart Crystals but only through lifestealing.");
        this.disableHeartCores = buildBoolean(builder, "Disable Heart Cores:", false, "Heart Cores can heal on default 25% of your health if right clicked. This value determines if they should be disabled.");
        this.disableEnchantments = buildBoolean(builder, "Disable Enchantments:", false, "This value determines whether modded enchantments from this mod should be disabled or not.");
        //this.bannedUponLosingAllHeartsOrLives = buildBoolean(builder, "Should Players get Banned When They Lose all Lives/Hearts:", false, "When this is false, players will go into spectator mode. Otherwise, players will get banned.");

        builder.pop();
        builder.comment("This category is everything related to life stealing from someone.");
        builder.push("Lifesteal Related");
        this.disableLifesteal = buildBoolean(builder, "Disable Lifesteal:", false, "This option changes the entire mod into more of a permanent heart gaining system. This makes it so nobody can gain hearts from lifestealing but ONLY through Heart Crystals. MOBS can still take your hearts away if they kill you though, UNLESS you have that option disabled.");
        this.playersGainHeartsifKillednoHeart = buildBoolean(builder, "Players Gain Hearts From No Heart Players:", false, "This value determines if a player should still earn hearts from a player they killed even if the player doesn't have hearts to spare. EX: MinimumHeartHave or disableHeartLoss settings");

        builder.pop();
        builder.comment("This category will hold the maximums for certain values");
        builder.push("Maximums and Minimums");
        this.maximumamountofheartsgainable = buildInt(builder, "Maximum Amount of Health/Hitpoints a Player can get:",  -1, -1, Integer.MAX_VALUE, "WARNING: THIS IS INCOMPATIBLE WITH LIVES. IF YOU ENABLE BOTH, LIVES WILL AUTOMATICALLY BE DISABLED. This value makes a limit SET after your Starting HitPoint Difference for how many hit points/hearts a player can get. 2 hit points = 1 heart. Set this to less than 1 to disable the feature.");
        this.minimumamountofheartscanhave = buildInt(builder,"Minimum Amount Of Health/Hitpoints a Player can have:", -1, -1, Integer.MAX_VALUE, "WARNING: THIS IS INCOMPATIBLE WITH LIVES. IF YOU ENABLE BOTH, LIVES WILL AUTOMATICALLY BE DISABLED. This value makes a limit set on how many hit points/hearts a player can at least have, this value is actually set depending on the Starting Health Difference. EX: Starting Health Difference - MinimumHeartHave. Set this to less than 0 to disable the feature.");

        builder.pop();
        builder.comment("This category holds values that aren't intended for gameplay and aren't polished, but can be used to test certain aspects of the mod easily or just for good fun.");
        builder.push("MISC/FUN");
        this.shouldAllMobsGiveHearts = buildBoolean(builder, "Killing any Mobs Gives Hearts:",  false, "When this is false, you can only gain hearts from killing players. Otherwise, any mob will give you hearts.");
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, double defaultValue, double min, double max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

}
