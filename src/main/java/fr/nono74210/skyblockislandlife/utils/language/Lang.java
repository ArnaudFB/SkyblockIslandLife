package fr.nono74210.skyblockislandlife.utils.language;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {

        PLUGIN_NAME("PluginName", "&9&l[&3&lSky&b&lIslandLife&9&l]"),
        DISPLAY_LIVES_LEFT("MiscMessages.DisplayLivesLeft", "&9&l[&3&lSky&b&lIslandLife&9&l] &7→ Vous avez %islandlife_left% vie(s) restante(s)"),
        DISPLAY_OTHER_PLAYER_LIVES_LEFT("MiscMessages.DisplayOtherPlayerLivesLeft", "&9&l[&3&lSky&b&lIslandLife&9&l] &7→ Le joueur %player% a %islandlife_left% vie(s) restante(s)"),
        LIVES_ADDED_SUCCESSFULLY("MiscMessages.LivesAddedSuccessfully","&9&l[&3&lSky&b&lIslandLife&9&l] &7→ Vous venez d''ajouter des vies au joueur : %player%"),
        LIVES_REMOVED_SUCCESSFULLY("MiscMessages.LivesRemovedSuccessfully","&9&l[&3&lSky&b&lIslandLife&9&l] &7→ Vous venez de retirer des vies au joueur : %player%"),
        LIVES_SET_SUCCESSFULLY("MiscMessages.LivesSetSuccessfully","&9&l[&3&lSky&b&lIslandLife&9&l] &7→ Vous venez de redéfinir le nombre de vie(s) au joueur : %player%"),
        VIE_COMMAND("HelpMessages.VieCommand", "/vie : permet de voir son nombre de vie(s) restante(s)"),
        RELOAD_COMMAND("HelpMessages.ReloadCommand", "/vie reload : permet de reload la config du plugin"),
        ADD_COMMAND("HelpMessages.AddCommand", "/vie add <joueur> <nombre> : permet d''ajouter des vies à un joueur"),
        REMOVE_COMMAND("HelpMessages.RemoveCommand", "/vie remove <joueur> <nombre> : permet de retirer des vies à un joueur"),
        SET_COMMAND("HelpMessages.SetCommand", "/vie set <joueur> <nombre> : permet de définir un nombre de vies à un joueur"),
        NO_PERMISSION_ERROR("ErrorMessages.NoPermissionError", "&9&l[&3&lSky&b&lIslandLife&9&l] &7→ &cVous n'avez pas la permission d'effectuer cette commande"),
        TOO_MANY_ARGS_ERROR("ErrorMessages.TooManyArgsError", "&9&l[&3&lSky&b&lIslandLife&9&l] &7→ &cTrop d'arguments pour la commande !"),
        NOT_ENOUGH_ARGS_ERROR("ErrorMessages.NotEnoughArgsError", "&9&l[&3&lSky&b&lIslandLife&9&l] &7→ &cPas assez d'arguments pour la commande !"),
        PLAYER_NOT_FOUND_ERROR("ErrorMessages.PlayerNotFoundError","&9&l[&3&lSky&b&lIslandLife&9&l] &7→ &cLe joueur spécifié n'a pas été trouvé"),
        NOT_AN_INT_ERROR("ErrorMessages.NotAnIntError","&9&l[&3&lSky&b&lIslandLife&9&l] &7→ &cVeuillez entrer un entier"),
        INVALID_INT_ERROR("ErrorMessages.InvalidIntError", "&9&l[&3&lSky&b&lIslandLife&9&l] &7→ &cVeuillez entrer un entier positif"),
        PLAYER_HAVE_NO_ISLAND("ErrorMessages.PlayerHaveNoIslandError", "&9&l[&3&lSky&b&lIslandLife&9&l] &7→ &cVous n''avez pas d''île pour le moment, créez en une avec /is create"),
        DATABASE_IN_ERROR("ErrorMessages.DatabaseInError","&9&l[&3&lSky&b&lIslandLife&9&l] &7→ &cBase de données injoignable, veuillez contacter un administrateur !");

        private String path;
        private String def;
        private static YamlConfiguration LANG;

        Lang(String path, String start) {
            this.path = path;
            this.def = start;
        }

        public static void setFile(YamlConfiguration config) {
            LANG = config;
        }

        @Override
        public String toString() {
            if (this == PLUGIN_NAME)
                return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def)) + " ";
            return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
        }

        public String getDefault() {
            return this.def;
        }

        public String getPath() {
            return this.path;
        }
    }