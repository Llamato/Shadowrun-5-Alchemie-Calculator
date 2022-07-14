package com.cinua.shadowrun5alchemiecalculator;
import android.content.Context;
import com.example.shadowrun5alchemiecalculator.R;

public class EnchantmentResultCard{
    public static final int COMMAND_TRIGGER_MALUS = 2;
    public static final int TOUCH_TRIGGER_MALUS = 1;
    public static final int TIMER_TRIGGER_MALUS = 2;

    public static final int NO_RESULT = 0;
    public static final int ENCHANTMENT_FAILED = 1;
    public static final int FAIL = 2;
    public static final int CRITICAL_FAIL = 3;
    public static final int SUCCESS = 4;

    private Context context;

    private int spellPowerLevel;
    private int spellDrain;
    private int triggerMalus;

    private int drainPool;
    private int magicRating;
    private int alchemyRating;
    private int mentalDamage;

    private long successThrows;
    private long failThrows;
    private long counterSuccesses;
    private long drainRollSuccesses;
    private int result = NO_RESULT;
    private int diceCount;

    private long drain;
    private long potency;

    private boolean edged = false;
    private boolean edgable = true;
    private boolean notEdgy = false;
    private long newSuccesses = 0;

    public EnchantmentResultCard(Context context, int spellPowerLevel, int spellDrain, int triggerMalus, int drainPool, int magicRating, int alchemyRating, int mentalDamage){
        this.context = context;
        this.spellPowerLevel = spellPowerLevel;
        this.spellDrain = spellDrain;
        this.triggerMalus = triggerMalus;
        this.mentalDamage = mentalDamage;

        this.drainPool = drainPool;
        this.magicRating = magicRating;
        this.alchemyRating = alchemyRating;
    }

    public String getPowerLevelString(){
        return String.format("%s: %d", context.getString(R.string.power_level), spellPowerLevel);
    }

    private long getCounterSuccesses(){
        return Dice.getSuccesses(Dice.roll(Dice.D6, spellPowerLevel));
    }

    private long getPotency(){
        if(edged){
            return potency;
        }
        potency = successThrows;
        if(potency > spellPowerLevel){
            potency = spellPowerLevel;
        }
        potency -= counterSuccesses;
        return potency;
    }

    public String getPotencyString(){
        if(!edged){
            return String.format("%s: %d (%d %s, %d %s)", context.getString(R.string.potency), getPotency(), successThrows, context.getString(R.string.successes), counterSuccesses, context.getString(R.string.perpetration_successes));
        }else{
            return String.format("%s: %d (%d+%d %s, %d %s)", context.getString(R.string.potency), getPotency(), successThrows, newSuccesses, context.getString(R.string.successes), counterSuccesses, context.getString(R.string.perpetration_successes));
        }
    }

    private long getSuccessfulDrainRolls(){
        return Dice.getSuccesses(Dice.roll(Dice.D6, drainPool));
    }

    private long getDrain(){
        if(edged){
            return drain;
        }
        drain = spellPowerLevel + spellDrain + triggerMalus - drainRollSuccesses;
        if(drain < 0){
            drain = 0;
        }
        return drain;
    }

    public long getMentalDamage(){
        return mentalDamage + getDrain();
    }

    public String getDrainString(){;
        return String.format("%s: %d (%d %s)", context.getString(R.string.drain), getDrain(), drainRollSuccesses, context.getString(R.string.drain_resisted));
    }

    public int getResult(){
        return result;
    }

    //Call this first or otherwise things go south..... Yes I do hate myself for writing it this way.
    public String getResultString(){
        if(edged){
            if (potency <= 0){
                result = ENCHANTMENT_FAILED;
                return context.getString(R.string.enchantment_failed);
            }
            result = SUCCESS;
            return context.getString(R.string.success);
        }
        diceCount = magicRating + alchemyRating - (int) Math.ceil(getMentalDamage() / 3f);
        if (diceCount < 0) { //Did Maya do that???? Is this my math being wrong?
            diceCount = 0;
        }
        int[] throwResults = Dice.roll(Dice.D6, diceCount);
        successThrows = Dice.getSuccesses(throwResults);
        failThrows = Dice.getFails(throwResults);
        counterSuccesses = getCounterSuccesses();
        drainRollSuccesses = getSuccessfulDrainRolls();
        if(failThrows >= Math.ceil(magicRating + alchemyRating) / 2){
            edgable = false;
            if(successThrows == 0){
                result = CRITICAL_FAIL;
                return context.getString(R.string.critical_fail);
            }
            result = FAIL;
            return context.getString(R.string.fail);
        }
        if(getPotency() <= 0){
            result = ENCHANTMENT_FAILED;
            return context.getString(R.string.enchantment_failed);
        }
        result = SUCCESS;
        return context.getString(R.string.success);
    }

    public void edge(){
        edged = true;
        edgable = false;
        newSuccesses = Dice.getSuccesses(Dice.roll(Dice.D6, (int) (diceCount - successThrows)));
        potency = successThrows + newSuccesses;
        if(potency > spellPowerLevel){
            potency = spellPowerLevel;
        }
        potency -= counterSuccesses;
    }

    public boolean isEdgeable(){
        return edgable;
    }

    public boolean isEdged(){
        return edged;
    }

    public boolean isNotEdgy(){
        return notEdgy;
    }

    public void setNotEdgy(){
        notEdgy = true;
    }
}
