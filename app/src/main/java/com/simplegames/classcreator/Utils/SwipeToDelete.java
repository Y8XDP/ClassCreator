package com.simplegames.classcreator.Utils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.simplegames.classcreator.adapters.ButtonHolder;
import com.simplegames.classcreator.adapters.MainAdapter;

import java.util.ArrayList;

public class SwipeToDelete extends ItemTouchHelper {

    public SwipeToDelete(final MainAdapter adapter, final RecyclerView recyclerView, final ArrayList<String> ids) {
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
                final int position = viewHolder.getAdapterPosition();

                adapter.removeItem(position);

                Snackbar snackbar = Snackbar.make(recyclerView, "Элемент удален", Snackbar.LENGTH_LONG)
                        .setAction("Отмена", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adapter.undoRemoveItem(position);
                            }
                        });

                snackbar.addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            adapter.updateDB();
                        }

                        super.onDismissed(transientBottomBar, event);
                    }
                });

                snackbar.show();
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
