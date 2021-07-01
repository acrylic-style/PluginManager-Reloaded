package xyz.acrylicstyle.plugin.utils

import org.bukkit.Bukkit

@Suppress("unused")
object Log {
    fun with(title: String?): java.util.logging.Logger {
        return java.util.logging.Logger.getLogger(title)
    }

    fun info(name: String, msg: String) {
        Bukkit.getLogger().info("[$name] $msg")
    }

    fun warning(name: String, msg: String) {
        Bukkit.getLogger().warning("[$name] $msg")
    }

    fun severe(name: String, msg: String) {
        Bukkit.getLogger().severe("[$name] $msg")
    }

    fun config(name: String, msg: String) {
        Bukkit.getLogger().config("[$name] $msg")
    }

    fun fine(name: String, msg: String) {
        Bukkit.getLogger().fine("[$name] $msg")
    }

    fun finer(name: String, msg: String) {
        Bukkit.getLogger().finer("[$name] $msg")
    }

    fun finest(name: String, msg: String) {
        Bukkit.getLogger().finest("[$name] $msg")
    }

    fun debug(name: String, msg: String) {
        Bukkit.getLogger().info("[$name] [DEBUG] $msg")
    }

    fun info(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            info(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun warning(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            warning(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun warn(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            warning(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun severe(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            severe(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun error(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            severe(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun config(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            config(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun fine(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            fine(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun finer(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            finer(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun finest(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            finest(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun debug(msg: String) {
        try {
            var name = Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            debug(name, msg)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun of(title: String): Logger {
        return Logger(title)
    }

    fun with(title: String): Logger {
        return Logger(title)
    }

    fun getLogger(title: String): Logger {
        return Logger(title)
    }

    val logger: Logger
        get() {
            var name: String
            name = try {
                Class.forName(Thread.currentThread().stackTrace[2].className).simpleName
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(e)
            }
            if (name.equals("", ignoreCase = true)) name = "Anonymous"
            return Logger(name)
        }

    class Logger(val name: String) {
        fun info(msg: String) {
            info(name, msg)
        }

        fun warning(msg: String) {
            warning(name, msg)
        }

        fun warn(msg: String) {
            warning(name, msg)
        }

        fun severe(msg: String) {
            severe(name, msg)
        }

        fun error(msg: String) {
            severe(name, msg)
        }

        fun config(msg: String) {
            config(name, msg)
        }

        fun fine(msg: String) {
            fine(name, msg)
        }

        fun finer(msg: String) {
            finer(name, msg)
        }

        fun finest(msg: String) {
            finest(name, msg)
        }

        fun debug(msg: String) {
            debug(name, msg)
        }
    }
}