package coffee.amo.astromancy.client.screen.stellalibri.pages;

import coffee.amo.astromancy.core.registration.BlockRegistration;
import coffee.amo.astromancy.core.registration.ItemRegistry;
import coffee.amo.astromancy.core.registration.ResearchRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.TreeMap;

import static coffee.amo.astromancy.core.registration.ItemRegistry.*;

public class ResearchPageRegistry {
    public static HashMap<ResourceLocation, TreeMap<Integer, BookPage>> pages = new HashMap<>();

    public static void registerPage(ResourceLocation research, int page, BookPage pageObject) {
        if (!pages.containsKey(research)) {
            pages.put(research, new TreeMap<>());
        }
        pages.get(research).put(page, pageObject);
    }

    public static void registerPages(ResourceLocation research, BookPage... pages){
        for(int i = 0; i < pages.length; i++){
            registerPage(research, i, pages[i]);
        }
    }

    public static TreeMap<Integer, BookPage> getPages(ResourceLocation research) {
        return pages.get(research);
    }

    public static void register(){
        registerPage(ResearchRegistry.INTRODUCTION.get().getResearchName(), 0, new HeadlineTextPage("introduction", "introduction.a", "introduction").setHidden(false));
        registerPage(ResearchRegistry.INTRODUCTION.get().getResearchName(), 1, new TextPage("introduction.b", "introduction").setHidden(false));
        registerPage(ResearchRegistry.STELLARITE.get().getResearchName(), 0, new HeadlineTextPage("stellarite", "stellarite.a", "stellarite").setHidden(false));
        registerPage(ResearchRegistry.ARCANA_SEQUENCE.get().getResearchName(), 0, new HeadlineTextPage("arcana_sequence", "arcana_sequence.a", "arcana_sequence").setHidden(false));
        registerPage(ResearchRegistry.ALCHEMICAL_BRASS.get().getResearchName(), 0, new HeadlineTextPage("alchemical_brass", "alchemical_brass.a", "alchemical_brass").setHidden(false));
        registerPages(ResearchRegistry.CRUCIBLE.get().getResearchName(), new HeadlineTextPage("crucible", "crucible.a", "crucible").setHidden(false), StructurePage.cruciblePage(BlockRegistration.CRUCIBLE.get()));

        registerPages(ResearchRegistry.ASPECTI_PHIAL.get().getResearchName(), new HeadlineTextPage("aspecti_phial", "aspecti_phial.a", "aspecti_phial").setHidden(false),
                new TextPage("aspecti_phial.b", "aspecti_phial").setHidden(false),
                CraftingPage.phialPage(ASPECTI_PHIAL.get()).setHidden(false));
        registerPage(ResearchRegistry.JAR.get().getResearchName(), 0, new HeadlineTextPage("jars", "jars.a", "jars").setHidden(false));

        registerPage(ResearchRegistry.STARGAZING.get().getResearchName(), 0, new HeadlineTextPage("stargazing", "stargazing.a", "stargazing").setHidden(false));
        registerPage(ResearchRegistry.SOLAR_ECLIPSE.get().getResearchName(), 0, new HeadlineTextPage("solar_eclipse", "solar_eclipse.a", "solar_eclipse").setHidden(false));
        registerPages(ResearchRegistry.ARMILLARY_SPHERE.get().getResearchName(),
                new HeadlineTextPage("armillary_sphere", "armillary_sphere.a", "armillary_sphere").setHidden(false),
                CraftingPage.armSpherePage(ARMILLARY_SPHERE.get(), ARMILLARY_SPHERE_CAGE.get(), ALCHEMICAL_BRASS_INGOT.get()).setHidden(true),
                CraftingPage.armCagePage(ARMILLARY_SPHERE_CAGE.get(), ALCHEMICAL_BRASS_INGOT.get()).setHidden(true));

        registerPage(ResearchRegistry.STELLAR_OBSERVATORY.get().getResearchName(), 0, new HeadlineTextPage("stellar_observatory", "stellar_observatory.a", "stellar_observatory").setHidden(false));
    }
}
