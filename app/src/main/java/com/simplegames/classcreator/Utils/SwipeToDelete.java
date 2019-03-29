package com.simplegames.classcreator.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.simplegames.classcreator.adapters.ButtonHolder;
import com.simplegames.classcreator.adapters.MainAdapter;

public class SwipeToDelete extends ItemTouchHelper {

    public SwipeToDelete(final MainAdapter adapter, RecyclerView recyclerView) {
        super(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                int position = viewHolder.getAdapterPosition();

                adapter.removeItem(position);
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof ButtonHolder) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                } else {
                    return 0;
                }
            }
        });

        attachToRecyclerView(recyclerView);
    }
}
