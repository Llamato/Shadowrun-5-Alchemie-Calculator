package com.cinua.shadowrun5alchemiecalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shadowrun5alchemiecalculator.R;
import com.google.android.material.card.MaterialCardView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class EnchantmentResultCardAdapter extends RecyclerView.Adapter<EnchantmentResultCardViewHolder>{
    private Context context;
    private ArrayList<EnchantmentResultCard> cards;
    private OnItemBoundListener onItemBoundListener;

    public EnchantmentResultCardAdapter(Context context, ArrayList<EnchantmentResultCard> cards){
        this.context = context;
        this.cards = cards;
    }

    @NonNull
    @NotNull
    @Override
    public EnchantmentResultCardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType){
        return new EnchantmentResultCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.enchantment_result_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EnchantmentResultCardViewHolder holder, int position){
        EnchantmentResultCard card = cards.get(position);
        String result = card.getResultString();
        holder.enchantmentResultLabel.setText(result);
        if(result.equals(context.getString(R.string.success))){
            holder.enchantmentResultLabel.setTextColor(context.getColor(R.color.success_color));
            ((MaterialCardView) holder.itemView).setStrokeColor(context.getColor(R.color.success_color));
        }else{
            holder.enchantmentResultLabel.setTextColor(context.getColor(R.color.fail_color));
            ((MaterialCardView) holder.itemView).setStrokeColor(context.getColor(R.color.fail_color));
        }
        holder.powerLevelLabel.setText(card.getPowerLevelString());
        holder.potencyLabel.setText(card.getPotencyString());
        holder.drainLabel.setText(card.getDrainString());
        if(card.isEdged()){
            holder.edgedLabel.setVisibility(View.VISIBLE);
        }
        if(card.isNotEdgy()){
            holder.edgedLabel.setText(R.string.not_edgy_enough);
            holder.edgedLabel.setVisibility(View.VISIBLE);
        }
        if(onItemBoundListener != null){
            onItemBoundListener.afterBind(position);
        }
    }

    @Override
    public int getItemCount(){
        return cards.size();
    }

    public void setOnItemBoundListener(OnItemBoundListener listener){
        this.onItemBoundListener = listener;
    }
}
