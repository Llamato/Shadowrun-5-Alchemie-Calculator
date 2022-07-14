package com.cinua.shadowrun5alchemiecalculator;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shadowrun5alchemiecalculator.R;
import org.jetbrains.annotations.NotNull;

public class EnchantmentResultCardViewHolder extends RecyclerView.ViewHolder{
    public TextView drainLabel;
    public TextView potencyLabel;
    public TextView edgedLabel;
    public TextView powerLevelLabel;
    public TextView enchantmentResultLabel;

    public EnchantmentResultCardViewHolder(@NonNull @NotNull View itemView){
        super(itemView);
        drainLabel = itemView.findViewById(R.id.drain_label);
        potencyLabel = itemView.findViewById(R.id.potency_label);
        edgedLabel = itemView.findViewById(R.id.edged_label);
        powerLevelLabel = itemView.findViewById(R.id.power_level_label);
        enchantmentResultLabel = itemView.findViewById(R.id.enchantment_result_label);
    }
}
