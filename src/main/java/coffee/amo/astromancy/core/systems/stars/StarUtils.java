package coffee.amo.astromancy.core.systems.stars;

import coffee.amo.astromancy.client.systems.ClientConstellationHolder;
import coffee.amo.astromancy.core.helpers.RomanNumeralHelper;
import coffee.amo.astromancy.core.systems.stars.classification.*;
import coffee.amo.astromancy.core.util.StarSavedData;
import com.mojang.datafixers.util.Pair;
import com.sammy.ortus.helpers.util.Color;
import net.minecraft.core.Direction;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class StarUtils {
    private static final List<String> luminosityClasses = List.of("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL", "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX", "L", "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX", "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX", "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX", "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC", "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C", "CI", "CII", "CIII", "CIV", "CV", "CVI", "CVII", "CVIII", "CIX", "CX", "CXI", "CXII", "CXIII", "CXIV", "CXV", "CX");
    private static final WeightedRandomList<StarClass> starClasses = WeightedRandomList.create(
            StarClass.HYPERGIANT,
            StarClass.SUPERGIANT,
            StarClass.GIANT,
            StarClass.MAIN_SEQUENCE,
            StarClass.DWARF,
            StarClass.WHITE_DWARF
    );

    public static Star generateStar(Level level){
        int spectralIntensity = level.random.nextInt(0, 50000);
        Star star = new Star(spectralIntensity);
        Constellations constellation = Constellations.values()[level.random.nextInt(0, Constellations.values().length)];
        int X = level.random.nextInt(11)-10;
        int Y = level.random.nextInt(11)-10;
        star.setName(constellation.getName() + " " + star.getLuminosityClass().getClassName() + star.getSpectralClass() + " " + RomanNumeralHelper.toRoman(star.getSpectralIntensity() / 100) + " [" + Math.abs(X) + " of " + getQuadrantFromX(X) + ", " + Math.abs(Y) + " of " + getQuadrantFromY(Y) + "]");
        StarSavedData.get(level.getServer()).addStar(star, X + 10, Y + 10, constellation);
        return star;
    }

    public static Color orangeOrBlue(int chance){
        if(chance == 1){
            return new Color(252,157,61,255);
        } else {
            return new Color(80, 228, 252, 255);
        }
    }

    public static Quadrants getQuadrantFromX(int x){
        if(x > 0){
            return Quadrants.PENTACLES;
        } else {
            return Quadrants.SWORDS;
        }
    }

    public static Quadrants getQuadrantFromY(int y){
        if(y > 0){
            return Quadrants.WANDS;
        } else {
            return Quadrants.CUPS;
        }
    }

    public static Star findStarByArcana(int x, int y, Constellations constellation){
        return StarSavedData.get().getStar(x, y, constellation);
    }

    public static Vec3 generatePosition(Star star, Level level){
        for(int x = 0; x < 20; x++){
            for(int z = 0; z < 20; z++){

                ConstellationInstance constellationInstance = ClientConstellationHolder.findConstellationFromStar(star);
                if(constellationInstance == null){
                    continue;
                }
                Star star1 = ClientConstellationHolder.getStar(x, z, constellationInstance.getConstellation());

                if(star1 != null){
                    return new Vec3((x - 10) / 10.0f, ClientConstellationHolder.findConstellationFromStar(star).getConstellation().getHeight(), (z - 10) / 10.0f);
                }
            }
        }
        return new Vec3(0, 0, 0);
    }
}
