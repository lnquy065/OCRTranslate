package com.bitstudio.aztranslate.adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class RecyclerBookmarkWordTouchHelper extends ItemTouchHelper.SimpleCallback
{
    public interface RecyclerBookmarkWordTouchHelperListener
    {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
    private float preDx = 0;
    private RecyclerBookmarkWordTouchHelperListener listener;

    public RecyclerBookmarkWordTouchHelper(int dragDirs, int swipeDirs, RecyclerBookmarkWordTouchHelperListener listener)
    {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection)
    {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        preDx = 0;
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState)
    {
        if (viewHolder != null)
        {
            final View foregroundView = ((BookmarkWordAdapter.MyViewHolder)viewHolder).viewForeground;
            //foregroundView.setBackgroundColor(Color.GREEN);
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        final View foregroundView = ((BookmarkWordAdapter.MyViewHolder)viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        final View backgroundView = ((BookmarkWordAdapter.MyViewHolder)viewHolder).viewBackground;
        final View foregroundView = ((BookmarkWordAdapter.MyViewHolder)viewHolder).viewForeground;
        if (preDx <= 0 && dX > 0)
            backgroundView.setBackgroundColor(Color.parseColor("#21991b"));
        else if (preDx >= 0 && dX < 0)
            backgroundView.setBackgroundColor(Color.RED);
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        final View foregroundView = ((BookmarkWordAdapter.MyViewHolder)viewHolder).viewForeground;
        //foregroundView.setBackgroundColor(Color.GRAY);
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }
}
