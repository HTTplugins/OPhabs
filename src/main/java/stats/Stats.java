package stats;

import users.OPUser;

// TODO: Se utilizará un Enum para modificar las estadísticas bases
// TODO: Se utilizará una interfaz a modo de componente para modificar otro tipo de estadísticas
// TODO:
public class Stats 
{
    private int level = 1;
    private double exp = 0;
    private double health = 0;
    private double damage = 0;
    private double armor = 0;

    public Stats(int level, double exp)
    {
        this.level = level;
        this.exp = exp;
    }

    public Stats(double baseHealth, double baseDamage, double baseArmor)
    {
        this.health = baseHealth;
        this.damage = baseDamage;
        this.armor = baseArmor;
    }

    public Stats(int level, double exp, double baseHealth, double baseDamage, double baseArmor)
    {
        this.level = level;
        this.exp = exp;
        this.health = baseHealth;
        this.damage = baseDamage;
        this.armor = baseArmor;
    }

    public int getLevel(){
        return level;
    }

    public double changeHealth(double healthChange)
    {
        this.health += healthChange;
        return this.health;
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

    public double getHealth()
    {
        return this.health;
    }

    public double getDamage()
    {
        return this.damage;
    }

    public double getArmor()
    {
        return this.armor;
    }

}
