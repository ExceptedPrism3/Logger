package me.prism3.logger.commands.getting;

import me.prism3.loggercore.database.data.Arguments;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class Pager {
    private final Long count;
    private final int limit;
    private final int pageLimit;
    private final int currentPage;

    /**
     * @param count       number of items
     * @param limit       rows per page
     * @param pageLimit   displayable number before skipping (page buttons)
     * @param currentPage current page (first index is 1)
     */
    public Pager(Long count, int limit,  int currentPage, int pageLimit) {
        this.count = count;
        this.limit = limit;
        this.pageLimit = pageLimit;
        this.currentPage = currentPage;
    }

    public TextComponent preparePaging(String commandLabel, Arguments arguments) {
        TextComponent textComponent = new TextComponent();
        if (hasPreviousPage()) {
            textComponent.addExtra(Pager.buildTextComponent("◄", "Previous page", arguments.forPreviousPage()));
        }
        if (this.hasNextPage()) {
            textComponent.addExtra(Pager.buildTextComponent("►", "Next page", arguments.forNextPage()));

        }

        return textComponent;
    }

    public boolean isFirstPage() {
        return this.getFirstPage() == this.currentPage;
    }

    public static TextComponent buildTextComponent(String display, String displayHover, String command) {
        TextComponent textComponent = new TextComponent();
        textComponent.setText(display);
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        HoverEvent hoverEvent =
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(displayHover).create());
        textComponent.setClickEvent(clickEvent);
        textComponent.setHoverEvent(hoverEvent);
        return textComponent;
    }


    public int getPrevPage() {
        return Math.max((this.currentPage - 1), 0);
    }

    public boolean isLastPage() {
        return this.getNumberOfPages() == this.currentPage;
    }

    public int getNextPage() {



        return Math.min(this.getNumberOfPages(), this.currentPage + 1);
    }

    /**
     * @return first page
     */
    public int getFirstPage() {
        return 1;
    }

    /**
     * @return last page
     */
    public int getLastPage() {
        return this.getNumberOfPages();
    }

    /**
     * @return number of pages
     */
    public int getNumberOfPages() {
        return ( int ) Math.ceil(( double ) count / ( double ) limit);
    }


    /**
     * @return count
     */
    public Long getCount() {
        return this.count;
    }


    /**
     * @return true if it has a next page
     */
    public boolean hasNextPage() {
        return this.currentPage < this.getNumberOfPages();
    }

    /**
     * @return true if it has a previous page
     */
    public boolean hasPreviousPage() {
        return this.currentPage > 1 ;
    }

}
