package stats;

import java.util.Map;
import java.util.HashMap;

import users.OPUser;

// TODO: Se utilizará un Enum para modificar las estadísticas bases
// TODO: Se utilizará una interfaz a modo de componente para modificar otro tipo de estadísticas
// TODO:
public class UserStats
{
    private final OPUser user;
    private Map<Integer, Stats> statsList;

    private double health = 20;
    private double damage = 1;
    private double armor = 1;
    public UserStats(OPUser user)
    {
        this(user, 20, 1, 1);
        statsList = new HashMap<>();
    }

    public UserStats(OPUser user, double baseHealth, double baseDamage, double baseArmor)
    {
        this.user = user;
        this.health = baseHealth;
        this.damage = baseDamage;
        this.armor = baseArmor;
    }

    // calcular
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
            this.user.getPlayer().setMaxHealth(this.health);
        }
        catch (Exception ignored)
        {
            //
        }
    }
    
    public void addStats(Integer id, Stats stats)
    {
        statsList.put(id, stats);
    }

    public void removeStats(Integer id)
    {
        statsList.remove(id);
    }

    public Stats getStats(Integer id)
    {
        return statsList.get(id);
    }

}
