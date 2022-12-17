package castSystem;

import htt.ophabs.OPhabs;
import org.bukkit.scheduler.BukkitRunnable;

public class coolDown {

    OPhabs plugin;

    private int
            BlackVoidYamiCD, Abilitie2YamiCD, Abilitie3YamiCD, Abilitie4YamiCD,
            Abilitie1MeraCD, Abilitie2MeraCD, Abilitie3MeraCD, Abilitie4MeraCD,
            EarthquakeGuraCD, CreateWaveGuraCD, HandVibrationGuraCD, ExpansionWaveGuraCD,
            LogiaBodyMokuCD, SmokeLegsMokuCD, SummonSmokerMokuCD, Abilitie4MokuCD,
            TransformationNekoReoparudoCD, FrontAttackNekoReoparudoCD, Abilitie3NekoReoparudoCD, Abilitie4NekoReoparudoCD;

    public coolDown(OPhabs plugin){
        this.plugin = plugin;
        BlackVoidYamiCD = Abilitie2YamiCD = Abilitie3YamiCD = Abilitie4YamiCD = 0;
        Abilitie1MeraCD = Abilitie2MeraCD = Abilitie3MeraCD = Abilitie4MeraCD = 0;
        EarthquakeGuraCD = CreateWaveGuraCD = HandVibrationGuraCD = ExpansionWaveGuraCD = 0;
        LogiaBodyMokuCD = SmokeLegsMokuCD = SummonSmokerMokuCD = Abilitie4MokuCD = 0;
        TransformationNekoReoparudoCD = FrontAttackNekoReoparudoCD = Abilitie3NekoReoparudoCD = Abilitie4NekoReoparudoCD = 0;
    }



    public void runCoolDownSystem(){

        new BukkitRunnable(){

            @Override
            public void run() {
                //YAMI
                if(BlackVoidYamiCD > 0)
                    BlackVoidYamiCD--;

                if(Abilitie2YamiCD > 0)
                    Abilitie2YamiCD--;

                if(Abilitie3YamiCD > 0)
                    Abilitie3YamiCD--;

                if(Abilitie4YamiCD > 0)
                    Abilitie4YamiCD--;

                //MERA
                if(Abilitie1MeraCD > 0)
                    Abilitie1MeraCD--;
                if(Abilitie2MeraCD > 0)
                    Abilitie2MeraCD--;
                if(Abilitie3MeraCD > 0)
                    Abilitie3MeraCD--;
                if(Abilitie4MeraCD > 0)
                    Abilitie4MeraCD--;

                //GURA
                if(EarthquakeGuraCD > 0)
                    EarthquakeGuraCD--;
                if(CreateWaveGuraCD > 0)
                    CreateWaveGuraCD--;
                if(HandVibrationGuraCD > 0)
                    HandVibrationGuraCD--;
                if(ExpansionWaveGuraCD > 0)
                    ExpansionWaveGuraCD--;


                //Moku
                if(LogiaBodyMokuCD > 0)
                    LogiaBodyMokuCD--;
                if(SmokeLegsMokuCD > 0)
                    SmokeLegsMokuCD--;
                if(SummonSmokerMokuCD > 0)
                    SummonSmokerMokuCD--;
                if(Abilitie4MokuCD > 0)
                    Abilitie4MokuCD--;

                //Reoparudo
                if(TransformationNekoReoparudoCD > 0)
                    TransformationNekoReoparudoCD--;
                if(FrontAttackNekoReoparudoCD > 0)
                    FrontAttackNekoReoparudoCD--;
                if(Abilitie3NekoReoparudoCD > 0)
                    Abilitie3NekoReoparudoCD--;
                if(Abilitie4NekoReoparudoCD > 0)
                    Abilitie4NekoReoparudoCD--;


            }


        }.runTaskTimer(plugin,0,20);

    }

    public void setBlackVoidYamiCD(int blackVoidYamiCD) {
        BlackVoidYamiCD = blackVoidYamiCD;
    }

    public void setAbilitie2YamiCD(int abilitie2YamiCD) {
        Abilitie2YamiCD = abilitie2YamiCD;
    }

    public void setAbilitie3YamiCD(int abilitie3YamiCD) {
        Abilitie3YamiCD = abilitie3YamiCD;
    }

    public void setAbilitie4YamiCD(int abilitie4YamiCD) {
        Abilitie4YamiCD = abilitie4YamiCD;
    }

    public void setAbilitie1MeraCD(int abilitie1MeraCD) {
        Abilitie1MeraCD = abilitie1MeraCD;
    }

    public void setAbilitie2MeraCD(int abilitie2MeraCD) {
        Abilitie2MeraCD = abilitie2MeraCD;
    }

    public void setAbilitie3MeraCD(int abilitie3MeraCD) {
        Abilitie3MeraCD = abilitie3MeraCD;
    }

    public void setAbilitie4MeraCD(int abilitie4MeraCD) {
        Abilitie4MeraCD = abilitie4MeraCD;
    }

    public void setEarthquakeGuraCD(int earthquakeGuraCD) {
        EarthquakeGuraCD = earthquakeGuraCD;
    }

    public void setCreateWaveGuraCD(int createWaveGuraCD) {
        CreateWaveGuraCD = createWaveGuraCD;
    }

    public void setHandVibrationGuraCD(int handVibrationGuraCD) {
        HandVibrationGuraCD = handVibrationGuraCD;
    }

    public void setExpansionWaveGuraCD(int expansionWaveGuraCD) {
        ExpansionWaveGuraCD = expansionWaveGuraCD;
    }

    public void setLogiaBodyMokuCD(int logiaBodyMokuCD) {
        LogiaBodyMokuCD = logiaBodyMokuCD;
    }

    public void setSmokeLegsMokuCD(int smokeLegsMokuCD) {
        SmokeLegsMokuCD = smokeLegsMokuCD;
    }

    public void setSummonSmokerMokuCD(int summonSmokerMokuCD) {
        SummonSmokerMokuCD = summonSmokerMokuCD;
    }

    public void setAbilitie4MokuCD(int abilitie4MokuCD) {
        Abilitie4MokuCD = abilitie4MokuCD;
    }

    public void setTransformationNekoReoparudoCD(int abilitie1NekoReoparudoCD) {
        TransformationNekoReoparudoCD = abilitie1NekoReoparudoCD;
    }

    public void setFrontAttack2NekoReoparudoCD(int FrontAttackNekoReoparudoCD) {
        FrontAttackNekoReoparudoCD = FrontAttackNekoReoparudoCD;
    }

    public void setAbilitie3NekoReoparudoCD(int abilitie3NekoReoparudoCD) {
        Abilitie3NekoReoparudoCD = abilitie3NekoReoparudoCD;
    }

    public void setAbilitie4NekoReoparudoCD(int abilitie4NekoReoparudoCD) {
        Abilitie4NekoReoparudoCD = abilitie4NekoReoparudoCD;
    }

    public int getBlackVoidYamiCD() {
        return BlackVoidYamiCD;
    }

    public int getAbilitie2YamiCD() {
        return Abilitie2YamiCD;
    }

    public int getAbilitie3YamiCD() {
        return Abilitie3YamiCD;
    }

    public int getAbilitie4YamiCD() {
        return Abilitie4YamiCD;
    }

    public int getAbilitie1MeraCD() {
        return Abilitie1MeraCD;
    }

    public int getAbilitie2MeraCD() {
        return Abilitie2MeraCD;
    }

    public int getAbilitie3MeraCD() {
        return Abilitie3MeraCD;
    }

    public int getAbilitie4MeraCD() {
        return Abilitie4MeraCD;
    }

    public int getEarthquakeGuraCD() {
        return EarthquakeGuraCD;
    }

    public int getCreateWaveGuraCD() {
        return CreateWaveGuraCD;
    }

    public int getHandVibrationGuraCD() {
        return HandVibrationGuraCD;
    }

    public int getExpansionWaveGuraCD() {
        return ExpansionWaveGuraCD;
    }

    public int getLogiaBodyMokuCD() {
        return LogiaBodyMokuCD;
    }

    public int getSmokeLegsMokuCD() {
        return SmokeLegsMokuCD;
    }

    public int getSummonSmokerMokuCD() {
        return SummonSmokerMokuCD;
    }

    public int getAbilitie4MokuCD() {
        return Abilitie4MokuCD;
    }

    public int getTransformationNekoReoparudoCD() {
        return TransformationNekoReoparudoCD;
    }

    public int getFrontAttackNekoReoparudoCD() {
        return FrontAttackNekoReoparudoCD;
    }

    public int getAbilitie3NekoReoparudoCD() {
        return Abilitie3NekoReoparudoCD;
    }

    public int getAbilitie4NekoReoparudoCD() {
        return Abilitie4NekoReoparudoCD;
    }
}
