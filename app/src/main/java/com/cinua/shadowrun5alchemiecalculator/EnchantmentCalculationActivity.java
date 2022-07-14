package com.cinua.shadowrun5alchemiecalculator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shadowrun5alchemiecalculator.R;

import java.util.ArrayList;

public class EnchantmentCalculationActivity extends AppCompatActivity{
    public static final String NAME_KEY = "name";
    public static final String MAGIC_KEY = "magic";
    public static final String SKILL_KEY = "skill";
    public static final String DRAIN_POOL_KEY = "drain-pool";
    public static final String STATE_MONITOR_KEY = "state-monitor";

    public EditText mentalDamageTextbox;
    private EditText skillBoniMaliTextbox;
    private EditText drainPoolBoniMaliTextbox;
    private Spinner triggerSpinner;
    private EditText spellPowerLevelTextbox;
    private EditText spellDrainTextbox;

    private ArrayList<EnchantmentResultCard> cards = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EnchantmentResultCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enchantment_calculation);
        Intent starter = getIntent();
        setTitle(String.format("%s: %s", getString(R.string.alchemie), starter.getStringExtra(NAME_KEY)));

        LinearLayout mentalDamageLayout = findViewById(R.id.mental_damage_section);
        TextView mentalDamageLabel = mentalDamageLayout.findViewById(R.id.section_title);
        mentalDamageLabel.setText(R.string.mental_damage);
        mentalDamageTextbox = mentalDamageLayout.findViewById(R.id.section_input);

        LinearLayout skillBoniMaliLayout = findViewById(R.id.skill_boni_mali_section);
        TextView skillBoniMaliLabel = skillBoniMaliLayout.findViewById(R.id.section_title);
        skillBoniMaliLabel.setText(R.string.skill_boni_mali);
        skillBoniMaliTextbox = skillBoniMaliLayout.findViewById(R.id.section_input);

        LinearLayout drainPoolBoniMaliLayout = findViewById(R.id.drain_pool_boni_mali_section);
        TextView drainPoolBoniMaliLabel = drainPoolBoniMaliLayout.findViewById(R.id.section_title);
        drainPoolBoniMaliLabel.setText(R.string.drain_pool_boni_mali);
        drainPoolBoniMaliTextbox = drainPoolBoniMaliLayout.findViewById(R.id.section_input);

        LinearLayout triggerLayout = findViewById(R.id.trigger_section);
        TextView triggerLabel = triggerLayout.findViewById(R.id.section_title);
        triggerLabel.setText(R.string.trigger);
        triggerSpinner = triggerLayout.findViewById(R.id.section_spinner);

        LinearLayout spellPowerLevelLayout = findViewById(R.id.spell_power_section);
        TextView spellPowerLevelLabel = spellPowerLevelLayout.findViewById(R.id.section_title);
        spellPowerLevelLabel.setText(R.string.power_level);
        spellPowerLevelTextbox = spellPowerLevelLayout.findViewById(R.id.section_input);

        LinearLayout spellDrainLayout = findViewById(R.id.spell_drain_section);
        TextView spellDrainLabel = spellDrainLayout.findViewById(R.id.section_title);
        spellDrainLabel.setText(R.string.spell_drain);
        spellDrainTextbox = spellDrainLayout.findViewById(R.id.section_input);

        Button enchantButton = findViewById(R.id.enchant_button);
        enchantButton.setOnClickListener(new EnchantButtonHandler());

        ImageButton foldButton = findViewById(R.id.fold_button);
        foldButton.setOnClickListener(new FoldButtonHandler());

        Button edgeButton = findViewById(R.id.edge_button);
        edgeButton.setOnClickListener(new EdgeButtonHandler());

        mRecyclerView = findViewById(R.id.card_list);
        mAdapter = new EnchantmentResultCardAdapter(this, cards);
        mAdapter.setOnItemBoundListener(new MentalDamageUpdater());
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private class EnchantButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View v){
            boolean error = false;
            Intent charAttributes = getIntent();
            if(!error){
                int triggerMalus = 0;
                String triggerString = (String) triggerSpinner.getSelectedItem();
                if(v.getContext().getString(R.string.touch).equals(triggerString)){
                    triggerMalus = EnchantmentResultCard.TOUCH_TRIGGER_MALUS;
                }else if(v.getContext().getString(R.string.command).equals(triggerString)){
                    triggerMalus = EnchantmentResultCard.COMMAND_TRIGGER_MALUS;
                }else if(v.getContext().getString(R.string.timer).equals(triggerString)){
                    triggerMalus = EnchantmentResultCard.TIMER_TRIGGER_MALUS;
                }
                EnchantmentResultCard card = new EnchantmentResultCard(
                        v.getContext(),
                        Integer.parseInt(spellPowerLevelTextbox.getText().toString()),
                        Integer.parseInt(spellDrainTextbox.getText().toString()),
                        triggerMalus,
                        charAttributes.getIntExtra(DRAIN_POOL_KEY, 0) + Integer.parseInt(drainPoolBoniMaliTextbox.getText().toString()),
                        charAttributes.getIntExtra(MAGIC_KEY, 0),
                        charAttributes.getIntExtra(SKILL_KEY, 0) + Integer.parseInt(skillBoniMaliTextbox.getText().toString()),
                        Integer.parseInt(mentalDamageTextbox.getText().toString())
                );
                cards.add(0, card);
                mAdapter.notifyItemInserted(0);
                mLayoutManager.scrollToPosition(0);
            }
        }
    }

    private class EdgeButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View v){
            if(cards.size() > 0){
                EnchantmentResultCard card = cards.get(0);
                if(card.isEdgeable()){
                    card.edge();
                    mAdapter.notifyItemChanged(0);
                }else{
                    card.setNotEdgy();
                    mAdapter.notifyItemChanged(0);
                }
            }
        }
    }

    private class FoldButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View v){
            LinearLayout foldableLayout = findViewById(R.id.foldable_layout);
            if(foldableLayout.getVisibility() == View.VISIBLE){
                foldableLayout.setVisibility(View.GONE);
                ((ImageButton) v).setImageResource(R.drawable.ic_baseline_arrow_downward_25);
            }else{
                foldableLayout.setVisibility(View.VISIBLE);
                ((ImageButton) v).setImageResource(R.drawable.ic_baseline_arrow_upward_25);
            }
        }
    }

    private class MentalDamageUpdater implements OnItemBoundListener{

        @Override
        public void afterBind(int position){
            mentalDamageTextbox.setText(String.valueOf(cards.get(position).getMentalDamage()));
        }
    }
}