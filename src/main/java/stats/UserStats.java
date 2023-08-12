package stats;

import users.OPUser;

// TODO: Se utilizará un Enum para modificar las estadísticas bases
// TODO: Se utilizará una interfaz a modo de componente para modificar otro tipo de estadísticas
// TODO:
public class UserStats
{
    private final OPUser user;
    private double health;
    private double damage;
    private double armor;

    public UserStats(OPUser user)
    {
        this(user, 20, 1, 0);
    }

    public UserStats(OPUser user, double baseHealth, double baseDamage, double baseArmor)
    {
        this.user = user;
        this.health = baseHealth;
        this.damage = baseDamage;
        this.armor = baseArmor;
    }

    public double changeDamage(double damageChange)
    {
        this.damage += damageChange;
        return this.damage;
    }

    public double changeArmor(double armorChange)
    {
        this.armor += armorChange;
        return this.armor;
    }

    public double getDamage()
    {
        return this.damage;
    }

    public double getArmor()
    {
        return this.armor;
    }

    public void apply()
    {
        try
        {
            // Aplicar las estadísticas al jugador

            // VIDA
            // this.user.getPlayer().setHealth(...)
        }
        catch (Exception ignored)
        {
            //
        }
    }
}
