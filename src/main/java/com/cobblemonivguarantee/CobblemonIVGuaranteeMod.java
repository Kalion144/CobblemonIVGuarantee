package com.cobblemonivguarantee;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.IVs;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CobblemonIVGuaranteeMod implements ModInitializer {

    public static final String MOD_ID = "cobblemonivguarantee";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Stats[] ALL_STATS = {
        Stats.HP,
        Stats.ATTACK,
        Stats.DEFENCE,
        Stats.SPECIAL_ATTACK,
        Stats.SPECIAL_DEFENCE,
        Stats.SPEED
    };

    public static final Set<String> SPECIAL_LABELS = Set.of(
        "legendary",
        "mythical",
        "ultra_beast",
        "ultrabeast",
        "paradox",
        "fossil"
    );

    public static final int MIN_PERFECT_IVS = 3;

    @Override
    public void onInitialize() {
        LOGGER.info("[CobblemonIVGuarantee] Mod carregado! Lendarios, miticos, Ultra Beasts, Paradox e fosseis receberao pelo menos 3 IVs perfeitos.");

        // Fossil Pokemon are revived via the Fossil Machine and go directly to the player's party.
        // MobEntity.initialize() is never called for them, so we must use this event instead.
        CobblemonEvents.FOSSIL_REVIVED.subscribe(event -> {
            Pokemon pokemon = event.getPokemon();
            if (pokemon == null) return;

            Species species = pokemon.getSpecies();
            if (species == null) return;

            if (!isSpecialPokemon(species)) return;

            guaranteeMinPerfectIVs(pokemon.getIvs(), MIN_PERFECT_IVS);

            LOGGER.info("[CobblemonIVGuarantee] {} revivido de fossil com IVs garantidos.", species.getName());
        });
    }

    public static boolean isSpecialPokemon(Species species) {
        Set<String> labels = species.getLabels();
        if (labels == null || labels.isEmpty()) return false;

        for (String label : labels) {
            if (SPECIAL_LABELS.contains(label.toLowerCase())) return true;
        }
        return false;
    }

    public static void guaranteeMinPerfectIVs(IVs ivs, int minCount) {
        List<Stats> notPerfect = new ArrayList<>(ALL_STATS.length);
        int perfectCount = 0;

        for (Stats stat : ALL_STATS) {
            if (ivs.get(stat) >= 31) {
                perfectCount++;
            } else {
                notPerfect.add(stat);
            }
        }

        if (perfectCount >= minCount) return;

        int needed = minCount - perfectCount;
        Collections.shuffle(notPerfect);

        for (int i = 0; i < needed && i < notPerfect.size(); i++) {
            ivs.set(notPerfect.get(i), 31);
        }
    }
}
