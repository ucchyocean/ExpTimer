/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 misterT2525
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.ucchyocean.et;

import java.util.Map;

import org.bukkit.Sound;

import com.google.common.collect.ImmutableMap;

/*
 * https://gist.github.com/misterT2525/2ef4257acce3477d3bb1
 */
public class CompatibleSound {

    public static final Sound AMBIENCE_CAVE         = get("AMBIENT_CAVE",                        "AMBIENCE_CAVE");
    public static final Sound AMBIENCE_RAIN         = get("WEATHER_RAIN",                        "AMBIENCE_RAIN");
    public static final Sound AMBIENCE_THUNDER      = get("ENTITY_LIGHTNING_THUNDER",            "AMBIENCE_THUNDER");
    public static final Sound FUSE                  = get("ENTITY_CREEPER_PRIMED",               "FUSE");
    public static final Sound DIG_WOOL              = get("BLOCK_CLOTH_BREAK",                   "DIG_WOOL");
    public static final Sound GLASS                 = get("BLOCK_GLASS_BREAK",                   "GLASS");
    public static final Sound DIG_GRASS             = get("BLOCK_GRASS_BREAK",                   "DIG_GRASS");
    public static final Sound DIG_GRAVEL            = get("BLOCK_GRAVEL_PLACE",                  "DIG_GRAVEL");
    public static final Sound DIG_SAND              = get("BLOCK_SAND_PLACE",                    "DIG_SAND");
    public static final Sound DIG_SNOW              = get("BLOCK_SNOW_BREAK",                    "DIG_SNOW");
    public static final Sound DIG_STONE             = get("BLOCK_STONE_BREAK",                   "DIG_STONE");
    public static final Sound DIG_WOOD              = get("BLOCK_WOOD_PLACE",                    "DIG_WOOD");
    public static final Sound FIRE                  = get("BLOCK_FIRE_AMBIENT",                  "FIRE");
    public static final Sound FIRE_IGNITE           = get("ITEM_FLINTANDSTEEL_USE",              "FIRE_IGNITE");
    public static final Sound FIREWORK_BLAST        = get("ENTITY_FIREWORK_BLAST",               "FIREWORK_BLAST");
    public static final Sound FIREWORK_BLAST2       = get("ENTITY_FIREWORK_BLAST_FAR",           "FIREWORK_BLAST2");
    public static final Sound FIREWORK_LARGE_BLAST  = get("ENTITY_FIREWORK_LARGE_BLAST",         "FIREWORK_LARGE_BLAST");
    public static final Sound FIREWORK_LARGE_BLAST2 = get("ENTITY_FIREWORK_LARGE_BLAST_FAR",     "FIREWORK_LARGE_BLAST2");
    public static final Sound FIREWORK_LAUNCH       = get("ENTITY_FIREWORK_LAUNCH",              "FIREWORK_LAUNCH");
    public static final Sound FIREWORK_TWINKLE      = get("ENTITY_FIREWORK_TWINKLE",             "FIREWORK_TWINKLE");
    public static final Sound FIREWORK_TWINKLE2     = get("ENTITY_FIREWORK_TWINKLE_FAR",         "FIREWORK_TWINKLE2");
    public static final Sound HURT_FLESH            = get("ENTITY_PLAYER_HURT",                  "HURT_FLESH");
    public static final Sound FALL_BIG              = get("ENTITY_GENERIC_BIG_FALL",             "FALL_BIG");
    public static final Sound FALL_SMALL            = get("ENTITY_PLAYER_SMALL_FALL",            "FALL_SMALL");
    public static final Sound SWIM                  = get("ENTITY_PLAYER_SWIM",                  "SWIM");
    public static final Sound SPLASH2               = get("ENTITY_HOSTILE_SPLASH",               "SPLASH2");
    public static final Sound LAVA                  = get("BLOCK_LAVA_AMBIENT",                  "LAVA");
    public static final Sound LAVA_POP              = get("BLOCK_LAVA_POP",                      "LAVA_POP");
    public static final Sound WATER                 = get("BLOCK_WATER_AMBIENT",                 "WATER");
    public static final Sound MINECART_BASE         = get("ENTITY_MINECART_RIDING",              "MINECART_BASE");
    public static final Sound MINECART_INSIDE       = get("ENTITY_MINECART_INSIDE",              "MINECART_INSIDE");
    public static final Sound BAT_DEATH             = get("ENTITY_BAT_DEATH",                    "BAT_DEATH");
    public static final Sound BAT_HURT              = get("ENTITY_BAT_HURT",                     "BAT_HURT");
    public static final Sound BAT_IDLE              = get("ENTITY_BAT_AMBIENT",                  "BAT_IDLE");
    public static final Sound BAT_LOOP              = get("ENTITY_BAT_LOOP",                     "BAT_LOOP");
    public static final Sound BAT_TAKEOFF           = get("ENTITY_BAT_TAKEOFF",                  "BAT_TAKEOFF");
    public static final Sound BLAZE_BREATH          = get("ENTITY_BLAZE_AMBIENT",                "BLAZE_BREATH");
    public static final Sound BLAZE_DEATH           = get("ENTITY_BLAZE_DEATH",                  "BLAZE_DEATH");
    public static final Sound BLAZE_HIT             = get("ENTITY_BLAZE_HURT",                   "BLAZE_HIT");
    public static final Sound CAT_HISS              = get("ENTITY_CAT_HISS",                     "CAT_HISS");
    public static final Sound CAT_HIT               = get("ENTITY_CAT_HURT",                     "CAT_HIT");
    public static final Sound CAT_MEOW              = get("ENTITY_CAT_AMBIENT",                  "CAT_MEOW");
    public static final Sound CAT_PURR              = get("ENTITY_CAT_PURR",                     "CAT_PURR");
    public static final Sound CAT_PURREOW           = get("ENTITY_CAT_PURREOW",                  "CAT_PURREOW");
    public static final Sound CHICKEN_HURT          = get("ENTITY_CHICKEN_HURT",                 "CHICKEN_HURT");
    public static final Sound CHICKEN_EGG_POP       = get("ENTITY_CHICKEN_EGG",                  "CHICKEN_EGG_POP");
    public static final Sound CHICKEN_IDLE          = get("ENTITY_CHICKEN_AMBIENT",              "CHICKEN_IDLE");
    public static final Sound CHICKEN_WALK          = get("ENTITY_CHICKEN_STEP",                 "CHICKEN_WALK");
    public static final Sound COW_HURT              = get("ENTITY_COW_HURT",                     "COW_HURT");
    public static final Sound COW_IDLE              = get("ENTITY_COW_AMBIENT",                  "COW_IDLE");
    public static final Sound COW_WALK              = get("ENTITY_COW_STEP",                     "COW_WALK");
    public static final Sound CREEPER_DEATH         = get("ENTITY_CREEPER_DEATH",                "CREEPER_DEATH");
    public static final Sound CREEPER_HISS          = get("ENTITY_CREEPER_HURT",                 "CREEPER_HISS");
    public static final Sound ENDERDRAGON_DEATH     = get("ENTITY_ENDERDRAGON_DEATH",            "ENDERDRAGON_DEATH");
    public static final Sound ENDERDRAGON_GROWL     = get("ENTITY_ENDERDRAGON_GROWL",            "ENDERDRAGON_GROWL");
    public static final Sound ENDERDRAGON_HIT       = get("ENTITY_ENDERDRAGON_HURT",             "ENDERDRAGON_HIT");
    public static final Sound ENDERDRAGON_WINGS     = get("ENTITY_ENDERDRAGON_FLAP",             "ENDERDRAGON_WINGS");
    public static final Sound ENDERMAN_DEATH        = get("ENTITY_ENDERMEN_DEATH",               "ENDERMAN_DEATH");
    public static final Sound ENDERMAN_HIT          = get("ENTITY_ENDERMEN_HURT",                "ENDERMAN_HIT");
    public static final Sound ENDERMAN_IDLE         = get("ENTITY_ENDERMEN_AMBIENT",             "ENDERMAN_IDLE");
    public static final Sound ENDERMAN_TELEPORT     = get("ENTITY_ENDERMEN_TELEPORT",            "ENDERMAN_TELEPORT");
    public static final Sound ENDERMAN_SCREAM       = get("ENTITY_ENDERMEN_SCREAM",              "ENDERMAN_SCREAM");
    public static final Sound ENDERMAN_STARE        = get("ENTITY_ENDERMEN_STARE",               "ENDERMAN_STARE");
    public static final Sound GHAST_SCREAM2         = get("ENTITY_GHAST_SCREAM",                 "GHAST_SCREAM2");
    public static final Sound GHAST_CHARGE          = get("ENTITY_GHAST_WARN",                   "GHAST_CHARGE");
    public static final Sound GHAST_DEATH           = get("ENTITY_GHAST_DEATH",                  "GHAST_DEATH");
    public static final Sound GHAST_FIREBALL        = get("ENTITY_GHAST_SHOOT",                  "GHAST_FIREBALL");
    public static final Sound GHAST_MOAN            = get("ENTITY_GHAST_AMBIENT",                "GHAST_MOAN");
    public static final Sound GHAST_SCREAM          = get("ENTITY_GHAST_HURT",                   "GHAST_SCREAM");
    public static final Sound HORSE_ANGRY           = get("ENTITY_HORSE_ANGRY",                  "HORSE_ANGRY");
    public static final Sound HORSE_ARMOR           = get("ENTITY_HORSE_ARMOR",                  "HORSE_ARMOR");
    public static final Sound HORSE_BREATHE         = get("ENTITY_HORSE_BREATHE",                "HORSE_BREATHE");
    public static final Sound HORSE_DEATH           = get("ENTITY_HORSE_DEATH",                  "HORSE_DEATH");
    public static final Sound DONKEY_ANGRY          = get("ENTITY_DONKEY_ANGRY",                 "DONKEY_ANGRY");
    public static final Sound DONKEY_DEATH          = get("ENTITY_DONKEY_DEATH",                 "DONKEY_DEATH");
    public static final Sound DONKEY_HIT            = get("ENTITY_DONKEY_HURT",                  "DONKEY_HIT");
    public static final Sound DONKEY_IDLE           = get("ENTITY_DONKEY_AMBIENT",               "DONKEY_IDLE");
    public static final Sound HORSE_GALLOP          = get("ENTITY_HORSE_GALLOP",                 "HORSE_GALLOP");
    public static final Sound HORSE_HIT             = get("ENTITY_HORSE_HURT",                   "HORSE_HIT");
    public static final Sound HORSE_IDLE            = get("ENTITY_HORSE_AMBIENT",                "HORSE_IDLE");
    public static final Sound HORSE_JUMP            = get("ENTITY_HORSE_JUMP",                   "HORSE_JUMP");
    public static final Sound HORSE_LAND            = get("ENTITY_HORSE_LAND",                   "HORSE_LAND");
    public static final Sound HORSE_SADDLE          = get("ENTITY_HORSE_SADDLE",                 "HORSE_SADDLE");
    public static final Sound HORSE_SKELETON_DEATH  = get("ENTITY_SKELETON_HORSE_DEATH",         "HORSE_SKELETON_DEATH");
    public static final Sound HORSE_SKELETON_HIT    = get("ENTITY_SKELETON_HORSE_HURT",          "HORSE_SKELETON_HIT");
    public static final Sound HORSE_SKELETON_IDLE   = get("ENTITY_SKELETON_HORSE_AMBIENT",       "HORSE_SKELETON_IDLE");
    public static final Sound HORSE_SOFT            = get("ENTITY_HORSE_STEP",                   "HORSE_SOFT");
    public static final Sound HORSE_WOOD            = get("ENTITY_HORSE_STEP_WOOD",              "HORSE_WOOD");
    public static final Sound HORSE_ZOMBIE_DEATH    = get("ENTITY_ZOMBIE_HORSE_DEATH",           "HORSE_ZOMBIE_DEATH");
    public static final Sound HORSE_ZOMBIE_HIT      = get("ENTITY_ZOMBIE_HORSE_HURT",            "HORSE_ZOMBIE_HIT");
    public static final Sound HORSE_ZOMBIE_IDLE     = get("ENTITY_ZOMBIE_HORSE_AMBIENT",         "HORSE_ZOMBIE_IDLE");
    public static final Sound IRONGOLEM_DEATH       = get("ENTITY_IRONGOLEM_DEATH",              "IRONGOLEM_DEATH");
    public static final Sound IRONGOLEM_HIT         = get("ENTITY_IRONGOLEM_HURT",               "IRONGOLEM_HIT");
    public static final Sound IRONGOLEM_THROW       = get("ENTITY_IRONGOLEM_ATTACK",             "IRONGOLEM_THROW");
    public static final Sound IRONGOLEM_WALK        = get("ENTITY_IRONGOLEM_STEP",               "IRONGOLEM_WALK");
    public static final Sound MAGMACUBE_WALK2       = get("ENTITY_MAGMACUBE_SQUISH",             "MAGMACUBE_WALK2");
    public static final Sound MAGMACUBE_JUMP        = get("ENTITY_MAGMACUBE_JUMP",               "MAGMACUBE_JUMP");
    public static final Sound MAGMACUBE_WALK        = get("ENTITY_SMALL_MAGMACUBE_SQUISH",       "MAGMACUBE_WALK");
    public static final Sound PIG_DEATH             = get("ENTITY_PIG_DEATH",                    "PIG_DEATH");
    public static final Sound PIG_IDLE              = get("ENTITY_PIG_HURT",                     "PIG_IDLE");
    public static final Sound PIG_WALK              = get("ENTITY_PIG_STEP",                     "PIG_WALK");
    public static final Sound SHEEP_IDLE            = get("ENTITY_SHEEP_AMBIENT",                "SHEEP_IDLE");
    public static final Sound SHEEP_SHEAR           = get("ENTITY_SHEEP_SHEAR",                  "SHEEP_SHEAR");
    public static final Sound SHEEP_WALK            = get("ENTITY_SHEEP_STEP",                   "SHEEP_WALK");
    public static final Sound SILVERFISH_HIT        = get("ENTITY_SILVERFISH_HURT",              "SILVERFISH_HIT");
    public static final Sound SILVERFISH_KILL       = get("ENTITY_SILVERFISH_DEATH",             "SILVERFISH_KILL");
    public static final Sound SILVERFISH_IDLE       = get("ENTITY_SILVERFISH_AMBIENT",           "SILVERFISH_IDLE");
    public static final Sound SILVERFISH_WALK       = get("ENTITY_SILVERFISH_STEP",              "SILVERFISH_WALK");
    public static final Sound SKELETON_DEATH        = get("ENTITY_SKELETON_DEATH",               "SKELETON_DEATH");
    public static final Sound SKELETON_HURT         = get("ENTITY_SKELETON_HURT",                "SKELETON_HURT");
    public static final Sound SKELETON_IDLE         = get("ENTITY_SKELETON_AMBIENT",             "SKELETON_IDLE");
    public static final Sound SKELETON_WALK         = get("ENTITY_SKELETON_STEP",                "SKELETON_WALK");
    public static final Sound SLIME_ATTACK          = get("ENTITY_SLIME_ATTACK",                 "SLIME_ATTACK");
    public static final Sound SLIME_WALK2           = get("BLOCK_SLIME_PLACE",                   "SLIME_WALK2");
    public static final Sound SLIME_WALK            = get("ENTITY_SMALL_SLIME_HURT",             "SLIME_WALK");
    public static final Sound SPIDER_DEATH          = get("ENTITY_SPIDER_DEATH",                 "SPIDER_DEATH");
    public static final Sound SPIDER_IDLE           = get("ENTITY_SPIDER_AMBIENT",               "SPIDER_IDLE");
    public static final Sound SPIDER_WALK           = get("ENTITY_SPIDER_STEP",                  "SPIDER_WALK");
    public static final Sound VILLAGER_DEATH        = get("ENTITY_VILLAGER_DEATH",               "VILLAGER_DEATH");
    public static final Sound VILLAGER_HAGGLE       = get("ENTITY_VILLAGER_TRADING",             "VILLAGER_HAGGLE");
    public static final Sound VILLAGER_HIT          = get("ENTITY_VILLAGER_HURT",                "VILLAGER_HIT");
    public static final Sound VILLAGER_IDLE         = get("ENTITY_VILLAGER_AMBIENT",             "VILLAGER_IDLE");
    public static final Sound VILLAGER_NO           = get("ENTITY_VILLAGER_NO",                  "VILLAGER_NO");
    public static final Sound VILLAGER_YES          = get("ENTITY_VILLAGER_YES",                 "VILLAGER_YES");
    public static final Sound WITHER_DEATH          = get("ENTITY_WITHER_DEATH",                 "WITHER_DEATH");
    public static final Sound WITHER_HURT           = get("ENTITY_WITHER_HURT",                  "WITHER_HURT");
    public static final Sound WITHER_IDLE           = get("ENTITY_WITHER_AMBIENT",               "WITHER_IDLE");
    public static final Sound WITHER_SHOOT          = get("ENTITY_WITHER_SHOOT",                 "WITHER_SHOOT");
    public static final Sound WITHER_SPAWN          = get("ENTITY_WITHER_SPAWN",                 "WITHER_SPAWN");
    public static final Sound WOLF_BARK             = get("ENTITY_WOLF_AMBIENT",                 "WOLF_BARK");
    public static final Sound WOLF_DEATH            = get("ENTITY_WOLF_DEATH",                   "WOLF_DEATH");
    public static final Sound WOLF_GROWL            = get("ENTITY_WOLF_GROWL",                   "WOLF_GROWL");
    public static final Sound WOLF_HOWL             = get("ENTITY_WOLF_HOWL",                    "WOLF_HOWL");
    public static final Sound WOLF_HURT             = get("ENTITY_WOLF_HURT",                    "WOLF_HURT");
    public static final Sound WOLF_PANT             = get("ENTITY_WOLF_PANT",                    "WOLF_PANT");
    public static final Sound WOLF_SHAKE            = get("ENTITY_WOLF_SHAKE",                   "WOLF_SHAKE");
    public static final Sound WOLF_WALK             = get("ENTITY_WOLF_STEP",                    "WOLF_WALK");
    public static final Sound WOLF_WHINE            = get("ENTITY_WOLF_WHINE",                   "WOLF_WHINE");
    public static final Sound ZOMBIE_DEATH          = get("ENTITY_ZOMBIE_DEATH",                 "ZOMBIE_DEATH");
    public static final Sound ZOMBIE_HURT           = get("ENTITY_ZOMBIE_HURT",                  "ZOMBIE_HURT");
    public static final Sound ZOMBIE_INFECT         = get("ENTITY_ZOMBIE_INFECT",                "ZOMBIE_INFECT");
    public static final Sound ZOMBIE_METAL          = get("ENTITY_ZOMBIE_ATTACK_IRON_DOOR",      "ZOMBIE_METAL");
    public static final Sound ZOMBIE_REMEDY         = get("ENTITY_ZOMBIE_VILLAGER_CURE",         "ZOMBIE_REMEDY");
    public static final Sound ZOMBIE_IDLE           = get("ENTITY_ZOMBIE_AMBIENT",               "ZOMBIE_IDLE");
    public static final Sound ZOMBIE_WALK           = get("ENTITY_ZOMBIE_STEP",                  "ZOMBIE_WALK");
    public static final Sound ZOMBIE_UNFECT         = get("ENTITY_ZOMBIE_VILLAGER_CONVERTED",    "ZOMBIE_UNFECT");
    public static final Sound ZOMBIE_WOOD           = get("ENTITY_ZOMBIE_ATTACK_DOOR_WOOD",      "ZOMBIE_WOOD");
    public static final Sound ZOMBIE_WOODBREAK      = get("ENTITY_ZOMBIE_BREAK_DOOR_WOOD",       "ZOMBIE_WOODBREAK");
    public static final Sound ZOMBIE_PIG_IDLE       = get("ENTITY_ZOMBIE_PIG_AMBIENT",           "ZOMBIE_PIG_IDLE");
    public static final Sound ZOMBIE_PIG_ANGRY      = get("ENTITY_ZOMBIE_PIG_ANGRY",             "ZOMBIE_PIG_ANGRY");
    public static final Sound ZOMBIE_PIG_DEATH      = get("ENTITY_ZOMBIE_PIG_DEATH",             "ZOMBIE_PIG_DEATH");
    public static final Sound ZOMBIE_PIG_HURT       = get("ENTITY_ZOMBIE_PIG_HURT",              "ZOMBIE_PIG_HURT");
    public static final Sound NOTE_BASS             = get("BLOCK_NOTE_BASS",                     "NOTE_BASS");
    public static final Sound NOTE_BASS_GUITAR      = get("BLOCK_NOTE_BASS",                     "NOTE_BASS_GUITAR");
    public static final Sound NOTE_BASS_DRUM        = get("BLOCK_NOTE_BASEDRUM",                 "NOTE_BASS_DRUM");
    public static final Sound NOTE_PIANO            = get("BLOCK_NOTE_HARP",                     "NOTE_PIANO");
    public static final Sound NOTE_STICKS           = get("BLOCK_NOTE_HAT",                      "NOTE_STICKS");
    public static final Sound NOTE_PLING            = get("BLOCK_NOTE_PLING",                    "NOTE_PLING");
    public static final Sound NOTE_SNARE_DRUM       = get("BLOCK_NOTE_SNARE",                    "NOTE_SNARE_DRUM");
    public static final Sound PORTAL                = get("BLOCK_PORTAL_AMBIENT",                "PORTAL");
    public static final Sound PORTAL_TRAVEL         = get("BLOCK_PORTAL_TRAVEL",                 "PORTAL_TRAVEL");
    public static final Sound PORTAL_TRIGGER        = get("BLOCK_PORTAL_TRIGGER",                "PORTAL_TRIGGER");
    public static final Sound ANVIL_BREAK           = get("BLOCK_ANVIL_DESTROY",                 "ANVIL_BREAK");
    public static final Sound ANVIL_LAND            = get("BLOCK_ANVIL_LAND",                    "ANVIL_LAND");
    public static final Sound ANVIL_USE             = get("BLOCK_ANVIL_USE",                     "ANVIL_USE");
    public static final Sound SHOOT_ARROW           = get("BLOCK_DISPENSER_LAUNCH",              "SHOOT_ARROW");
    public static final Sound ARROW_HIT             = get("BLOCK_TRIPWIRE_DETACH",               "ARROW_HIT");
    public static final Sound ITEM_BREAK            = get("ENTITY_ITEM_BREAK",                   "ITEM_BREAK");
    public static final Sound BURP                  = get("ENTITY_PLAYER_BURP",                  "BURP");
    public static final Sound CHEST_CLOSE           = get("BLOCK_ENDERCHEST_CLOSE",              "CHEST_CLOSE");
    public static final Sound CHEST_OPEN            = get("BLOCK_ENDERCHEST_OPEN",               "CHEST_OPEN");
    public static final Sound CLICK                 = get("BLOCK_TRIPWIRE_CLICK_ON",             "CLICK");
    public static final Sound DOOR_CLOSE            = get("BLOCK_WOODEN_DOOR_CLOSE",             "DOOR_CLOSE");
    public static final Sound DOOR_OPEN             = get("BLOCK_WOODEN_DOOR_OPEN",              "DOOR_OPEN");
    public static final Sound DRINK                 = get("ENTITY_GENERIC_DRINK",                "DRINK");
    public static final Sound EAT                   = get("ENTITY_GENERIC_EAT",                  "EAT");
    public static final Sound EXPLODE               = get("ENTITY_ENDERDRAGON_FIREBALL_EXPLODE", "EXPLODE");
    public static final Sound FIZZ                  = get("BLOCK_LAVA_EXTINGUISH",               "FIZZ");
    public static final Sound LEVEL_UP              = get("ENTITY_PLAYER_LEVELUP",               "LEVEL_UP");
    public static final Sound ORB_PICKUP            = get("ENTITY_EXPERIENCE_ORB_PICKUP",        "ORB_PICKUP");
    public static final Sound ITEM_PICKUP           = get("ENTITY_ITEM_PICKUP",                  "ITEM_PICKUP");
    public static final Sound SPLASH                = get("ENTITY_BOBBER_SPLASH",                "SPLASH");
    public static final Sound SUCCESSFUL_HIT        = get("ENTITY_ARROW_HIT_PLAYER",             "SUCCESSFUL_HIT");
    public static final Sound WOOD_CLICK            = get("BLOCK_WOOD_BUTTON_CLICK_OFF",         "WOOD_CLICK");
    public static final Sound STEP_WOOL             = get("BLOCK_CLOTH_STEP",                    "STEP_WOOL");
    public static final Sound STEP_GRASS            = get("BLOCK_GRASS_STEP",                    "STEP_GRASS");
    public static final Sound STEP_GRAVEL           = get("BLOCK_GRAVEL_STEP",                   "STEP_GRAVEL");
    public static final Sound STEP_LADDER           = get("BLOCK_LADDER_STEP",                   "STEP_LADDER");
    public static final Sound STEP_SAND             = get("BLOCK_SAND_STEP",                     "STEP_SAND");
    public static final Sound STEP_SNOW             = get("BLOCK_SNOW_STEP",                     "STEP_SNOW");
    public static final Sound STEP_STONE            = get("BLOCK_STONE_STEP",                    "STEP_STONE");
    public static final Sound STEP_WOOD             = get("BLOCK_WOOD_STEP",                     "STEP_WOOD");
    public static final Sound PISTON_RETRACT        = get("BLOCK_PISTON_CONTRACT",               "PISTON_RETRACT");
    public static final Sound PISTON_EXTEND         = get("BLOCK_PISTON_EXTEND",                 "PISTON_EXTEND");

    public static Sound valueOf(String name) {
        if (name == null) throw new NullPointerException("Name is null");
        if (!map.containsKey(name)) throw new IllegalArgumentException("No enum constant Sound." + name);
        return map.get(name);
    }

    public static Sound valueOf(String name, Sound def) {
        if (name == null) return def;
        if (!map.containsKey(name)) return def;
        return map.get(name);
    }

    private static final Map<String, Sound> map;

    static {
        ImmutableMap.Builder<String, Sound> builder = ImmutableMap.builder();
        builder.put("AMBIENCE_CAVE",         AMBIENCE_CAVE);
        builder.put("AMBIENCE_RAIN",         AMBIENCE_RAIN);
        builder.put("AMBIENCE_THUNDER",      AMBIENCE_THUNDER);
        builder.put("FUSE",                  FUSE);
        builder.put("DIG_WOOL",              DIG_WOOL);
        builder.put("GLASS",                 GLASS);
        builder.put("DIG_GRASS",             DIG_GRASS);
        builder.put("DIG_GRAVEL",            DIG_GRAVEL);
        builder.put("DIG_SAND",              DIG_SAND);
        builder.put("DIG_SNOW",              DIG_SNOW);
        builder.put("DIG_STONE",             DIG_STONE);
        builder.put("DIG_WOOD",              DIG_WOOD);
        builder.put("FIRE",                  FIRE);
        builder.put("FIRE_IGNITE",           FIRE_IGNITE);
        builder.put("FIREWORK_BLAST",        FIREWORK_BLAST);
        builder.put("FIREWORK_BLAST2",       FIREWORK_BLAST2);
        builder.put("FIREWORK_LARGE_BLAST",  FIREWORK_LARGE_BLAST);
        builder.put("FIREWORK_LARGE_BLAST2", FIREWORK_LARGE_BLAST2);
        builder.put("FIREWORK_LAUNCH",       FIREWORK_LAUNCH);
        builder.put("FIREWORK_TWINKLE",      FIREWORK_TWINKLE);
        builder.put("FIREWORK_TWINKLE2",     FIREWORK_TWINKLE2);
        builder.put("HURT_FLESH",            HURT_FLESH);
        builder.put("FALL_BIG",              FALL_BIG);
        builder.put("FALL_SMALL",            FALL_SMALL);
        builder.put("SWIM",                  SWIM);
        builder.put("SPLASH2",               SPLASH2);
        builder.put("LAVA",                  LAVA);
        builder.put("LAVA_POP",              LAVA_POP);
        builder.put("WATER",                 WATER);
        builder.put("MINECART_BASE",         MINECART_BASE);
        builder.put("MINECART_INSIDE",       MINECART_INSIDE);
        builder.put("BAT_DEATH",             BAT_DEATH);
        builder.put("BAT_HURT",              BAT_HURT);
        builder.put("BAT_IDLE",              BAT_IDLE);
        builder.put("BAT_LOOP",              BAT_LOOP);
        builder.put("BAT_TAKEOFF",           BAT_TAKEOFF);
        builder.put("BLAZE_BREATH",          BLAZE_BREATH);
        builder.put("BLAZE_DEATH",           BLAZE_DEATH);
        builder.put("BLAZE_HIT",             BLAZE_HIT);
        builder.put("CAT_HISS",              CAT_HISS);
        builder.put("CAT_HIT",               CAT_HIT);
        builder.put("CAT_MEOW",              CAT_MEOW);
        builder.put("CAT_PURR",              CAT_PURR);
        builder.put("CAT_PURREOW",           CAT_PURREOW);
        builder.put("CHICKEN_HURT",          CHICKEN_HURT);
        builder.put("CHICKEN_EGG_POP",       CHICKEN_EGG_POP);
        builder.put("CHICKEN_IDLE",          CHICKEN_IDLE);
        builder.put("CHICKEN_WALK",          CHICKEN_WALK);
        builder.put("COW_HURT",              COW_HURT);
        builder.put("COW_IDLE",              COW_IDLE);
        builder.put("COW_WALK",              COW_WALK);
        builder.put("CREEPER_DEATH",         CREEPER_DEATH);
        builder.put("CREEPER_HISS",          CREEPER_HISS);
        builder.put("ENDERDRAGON_DEATH",     ENDERDRAGON_DEATH);
        builder.put("ENDERDRAGON_GROWL",     ENDERDRAGON_GROWL);
        builder.put("ENDERDRAGON_HIT",       ENDERDRAGON_HIT);
        builder.put("ENDERDRAGON_WINGS",     ENDERDRAGON_WINGS);
        builder.put("ENDERMAN_DEATH",        ENDERMAN_DEATH);
        builder.put("ENDERMAN_HIT",          ENDERMAN_HIT);
        builder.put("ENDERMAN_IDLE",         ENDERMAN_IDLE);
        builder.put("ENDERMAN_TELEPORT",     ENDERMAN_TELEPORT);
        builder.put("ENDERMAN_SCREAM",       ENDERMAN_SCREAM);
        builder.put("ENDERMAN_STARE",        ENDERMAN_STARE);
        builder.put("GHAST_SCREAM2",         GHAST_SCREAM2);
        builder.put("GHAST_CHARGE",          GHAST_CHARGE);
        builder.put("GHAST_DEATH",           GHAST_DEATH);
        builder.put("GHAST_FIREBALL",        GHAST_FIREBALL);
        builder.put("GHAST_MOAN",            GHAST_MOAN);
        builder.put("GHAST_SCREAM",          GHAST_SCREAM);
        builder.put("HORSE_ANGRY",           HORSE_ANGRY);
        builder.put("HORSE_ARMOR",           HORSE_ARMOR);
        builder.put("HORSE_BREATHE",         HORSE_BREATHE);
        builder.put("HORSE_DEATH",           HORSE_DEATH);
        builder.put("DONKEY_ANGRY",          DONKEY_ANGRY);
        builder.put("DONKEY_DEATH",          DONKEY_DEATH);
        builder.put("DONKEY_HIT",            DONKEY_HIT);
        builder.put("DONKEY_IDLE",           DONKEY_IDLE);
        builder.put("HORSE_GALLOP",          HORSE_GALLOP);
        builder.put("HORSE_HIT",             HORSE_HIT);
        builder.put("HORSE_IDLE",            HORSE_IDLE);
        builder.put("HORSE_JUMP",            HORSE_JUMP);
        builder.put("HORSE_LAND",            HORSE_LAND);
        builder.put("HORSE_SADDLE",          HORSE_SADDLE);
        builder.put("HORSE_SKELETON_DEATH",  HORSE_SKELETON_DEATH);
        builder.put("HORSE_SKELETON_HIT",    HORSE_SKELETON_HIT);
        builder.put("HORSE_SKELETON_IDLE",   HORSE_SKELETON_IDLE);
        builder.put("HORSE_SOFT",            HORSE_SOFT);
        builder.put("HORSE_WOOD",            HORSE_WOOD);
        builder.put("HORSE_ZOMBIE_DEATH",    HORSE_ZOMBIE_DEATH);
        builder.put("HORSE_ZOMBIE_HIT",      HORSE_ZOMBIE_HIT);
        builder.put("HORSE_ZOMBIE_IDLE",     HORSE_ZOMBIE_IDLE);
        builder.put("IRONGOLEM_DEATH",       IRONGOLEM_DEATH);
        builder.put("IRONGOLEM_HIT",         IRONGOLEM_HIT);
        builder.put("IRONGOLEM_THROW",       IRONGOLEM_THROW);
        builder.put("IRONGOLEM_WALK",        IRONGOLEM_WALK);
        builder.put("MAGMACUBE_WALK2",       MAGMACUBE_WALK2);
        builder.put("MAGMACUBE_JUMP",        MAGMACUBE_JUMP);
        builder.put("MAGMACUBE_WALK",        MAGMACUBE_WALK);
        builder.put("PIG_DEATH",             PIG_DEATH);
        builder.put("PIG_IDLE",              PIG_IDLE);
        builder.put("PIG_WALK",              PIG_WALK);
        builder.put("SHEEP_IDLE",            SHEEP_IDLE);
        builder.put("SHEEP_SHEAR",           SHEEP_SHEAR);
        builder.put("SHEEP_WALK",            SHEEP_WALK);
        builder.put("SILVERFISH_HIT",        SILVERFISH_HIT);
        builder.put("SILVERFISH_KILL",       SILVERFISH_KILL);
        builder.put("SILVERFISH_IDLE",       SILVERFISH_IDLE);
        builder.put("SILVERFISH_WALK",       SILVERFISH_WALK);
        builder.put("SKELETON_DEATH",        SKELETON_DEATH);
        builder.put("SKELETON_HURT",         SKELETON_HURT);
        builder.put("SKELETON_IDLE",         SKELETON_IDLE);
        builder.put("SKELETON_WALK",         SKELETON_WALK);
        builder.put("SLIME_ATTACK",          SLIME_ATTACK);
        builder.put("SLIME_WALK2",           SLIME_WALK2);
        builder.put("SLIME_WALK",            SLIME_WALK);
        builder.put("SPIDER_DEATH",          SPIDER_DEATH);
        builder.put("SPIDER_IDLE",           SPIDER_IDLE);
        builder.put("SPIDER_WALK",           SPIDER_WALK);
        builder.put("VILLAGER_DEATH",        VILLAGER_DEATH);
        builder.put("VILLAGER_HAGGLE",       VILLAGER_HAGGLE);
        builder.put("VILLAGER_HIT",          VILLAGER_HIT);
        builder.put("VILLAGER_IDLE",         VILLAGER_IDLE);
        builder.put("VILLAGER_NO",           VILLAGER_NO);
        builder.put("VILLAGER_YES",          VILLAGER_YES);
        builder.put("WITHER_DEATH",          WITHER_DEATH);
        builder.put("WITHER_HURT",           WITHER_HURT);
        builder.put("WITHER_IDLE",           WITHER_IDLE);
        builder.put("WITHER_SHOOT",          WITHER_SHOOT);
        builder.put("WITHER_SPAWN",          WITHER_SPAWN);
        builder.put("WOLF_BARK",             WOLF_BARK);
        builder.put("WOLF_DEATH",            WOLF_DEATH);
        builder.put("WOLF_GROWL",            WOLF_GROWL);
        builder.put("WOLF_HOWL",             WOLF_HOWL);
        builder.put("WOLF_HURT",             WOLF_HURT);
        builder.put("WOLF_PANT",             WOLF_PANT);
        builder.put("WOLF_SHAKE",            WOLF_SHAKE);
        builder.put("WOLF_WALK",             WOLF_WALK);
        builder.put("WOLF_WHINE",            WOLF_WHINE);
        builder.put("ZOMBIE_DEATH",          ZOMBIE_DEATH);
        builder.put("ZOMBIE_HURT",           ZOMBIE_HURT);
        builder.put("ZOMBIE_INFECT",         ZOMBIE_INFECT);
        builder.put("ZOMBIE_METAL",          ZOMBIE_METAL);
        builder.put("ZOMBIE_REMEDY",         ZOMBIE_REMEDY);
        builder.put("ZOMBIE_IDLE",           ZOMBIE_IDLE);
        builder.put("ZOMBIE_WALK",           ZOMBIE_WALK);
        builder.put("ZOMBIE_UNFECT",         ZOMBIE_UNFECT);
        builder.put("ZOMBIE_WOOD",           ZOMBIE_WOOD);
        builder.put("ZOMBIE_WOODBREAK",      ZOMBIE_WOODBREAK);
        builder.put("ZOMBIE_PIG_IDLE",       ZOMBIE_PIG_IDLE);
        builder.put("ZOMBIE_PIG_ANGRY",      ZOMBIE_PIG_ANGRY);
        builder.put("ZOMBIE_PIG_DEATH",      ZOMBIE_PIG_DEATH);
        builder.put("ZOMBIE_PIG_HURT",       ZOMBIE_PIG_HURT);
        builder.put("NOTE_BASS",             NOTE_BASS);
        builder.put("NOTE_BASS_GUITAR",      NOTE_BASS_GUITAR);
        builder.put("NOTE_BASS_DRUM",        NOTE_BASS_DRUM);
        builder.put("NOTE_PIANO",            NOTE_PIANO);
        builder.put("NOTE_STICKS",           NOTE_STICKS);
        builder.put("NOTE_PLING",            NOTE_PLING);
        builder.put("NOTE_SNARE_DRUM",       NOTE_SNARE_DRUM);
        builder.put("PORTAL",                PORTAL);
        builder.put("PORTAL_TRAVEL",         PORTAL_TRAVEL);
        builder.put("PORTAL_TRIGGER",        PORTAL_TRIGGER);
        builder.put("ANVIL_BREAK",           ANVIL_BREAK);
        builder.put("ANVIL_LAND",            ANVIL_LAND);
        builder.put("ANVIL_USE",             ANVIL_USE);
        builder.put("SHOOT_ARROW",           SHOOT_ARROW);
        builder.put("ARROW_HIT",             ARROW_HIT);
        builder.put("ITEM_BREAK",            ITEM_BREAK);
        builder.put("BURP",                  BURP);
        builder.put("CHEST_CLOSE",           CHEST_CLOSE);
        builder.put("CHEST_OPEN",            CHEST_OPEN);
        builder.put("CLICK",                 CLICK);
        builder.put("DOOR_CLOSE",            DOOR_CLOSE);
        builder.put("DOOR_OPEN",             DOOR_OPEN);
        builder.put("DRINK",                 DRINK);
        builder.put("EAT",                   EAT);
        builder.put("EXPLODE",               EXPLODE);
        builder.put("FIZZ",                  FIZZ);
        builder.put("LEVEL_UP",              LEVEL_UP);
        builder.put("ORB_PICKUP",            ORB_PICKUP);
        builder.put("ITEM_PICKUP",           ITEM_PICKUP);
        builder.put("SPLASH",                SPLASH);
        builder.put("SUCCESSFUL_HIT",        SUCCESSFUL_HIT);
        builder.put("WOOD_CLICK",            WOOD_CLICK);
        builder.put("STEP_WOOL",             STEP_WOOL);
        builder.put("STEP_GRASS",            STEP_GRASS);
        builder.put("STEP_GRAVEL",           STEP_GRAVEL);
        builder.put("STEP_LADDER",           STEP_LADDER);
        builder.put("STEP_SAND",             STEP_SAND);
        builder.put("STEP_SNOW",             STEP_SNOW);
        builder.put("STEP_STONE",            STEP_STONE);
        builder.put("STEP_WOOD",             STEP_WOOD);
        builder.put("PISTON_RETRACT",        PISTON_RETRACT);
        builder.put("PISTON_EXTEND",         PISTON_EXTEND);
        map = builder.build();
    }


    public CompatibleSound() {}

    private static boolean isLegacy = false;

    private static Sound get(String now, String legacy) {
        try {
            return Sound.valueOf(isLegacy ? legacy : now);
        } catch (IllegalArgumentException e) {
            isLegacy = !isLegacy;
            return Sound.valueOf(isLegacy ? legacy : now);
        }
    }
}

