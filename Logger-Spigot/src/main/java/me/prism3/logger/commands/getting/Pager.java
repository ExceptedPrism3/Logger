package me.prism3.logger.commands.getting;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

@SuppressWarnings("deprecation")
public class Pager {
  private final Long count;
  private final int limit;
  private final int pageLimit;
  private final int currentPage;

  /**
   * @param count       number of items
   * @param limit       rows per page
   * @param pageLimit   displayable number before skipping (page buttons)
   * @param currentPage current page (first index is 0)
   */
  public Pager(Long count, int limit, int pageLimit, int currentPage) {
    this.count = count;
    this.limit = limit;
    this.pageLimit = pageLimit;
    this.currentPage = currentPage;
  }

  public TextComponent preparePaging(String commandLabel, String searchedPlayer) {
    // TODO formatting textcomponent

    TextComponent pagerList = new TextComponent();
    if (this.count == 0) {
      pagerList.setText("No results found!");
      return pagerList;
    }
    pagerList.setBold(true);

    TextComponent firstPage = this.prepareFirstPage(searchedPlayer);
    pagerList.addExtra(firstPage);

    for (int i = this.getPaginationStart() + 1; i < this.getPaginationEnd(); i++) {
      int pageNumber = i + 1;
      TextComponent pageBtn = new TextComponent(String.valueOf(pageNumber));

      String commandString = Pager.buildCommand(commandLabel, searchedPlayer, pageNumber);

      String hoverString = "page " + pageNumber;

      ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandString);
      HoverEvent hoverEvent =
          new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverString).create());

      if (i == currentPage) {
        pageBtn.setUnderlined(true);
      } else {
        pageBtn.setClickEvent(clickEvent);
        pageBtn.setHoverEvent(hoverEvent);
      }

      pagerList.addExtra(pageBtn);
      if (this.hasNextPage(i)) {
        pagerList.addExtra(Pager.getSeperatorAsComponent());
      }
    }
    if (this.getLastPage() != this.getFirstPage()) {
      TextComponent lastPage = this.prepareLastPage(searchedPlayer);
      pagerList.addExtra(lastPage);
    }

    return pagerList;
  }

  /**
   * 
   * @param searchedPlayer searched player
   * @return First page button
   * 
   */
  public TextComponent prepareFirstPage(String searchedPlayer) {
    // FIXME formatting issue 
    String firstCommand = "/loggerget " + searchedPlayer + " " + this.getFirstPage();

    TextComponent start = new TextComponent();

    start.setText("First...");
    if (this.currentPage <= this.pageLimit) {
      start.setText("First");

    }
    if (0 == currentPage) {
      start.setUnderlined(true);
    } else {
      start.setHoverEvent(
          new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(firstCommand).create()));
      start.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, firstCommand));

    }

    if (hasNextPage(this.getFirstPage())) {
      start.addExtra(Pager.getSeperatorAsComponent());
    }
    return start;
  }

  /**
   * 
   * @param searchedPlayer searched Player
   * @return Last page button
   */

  public TextComponent prepareLastPage(String searchedPlayer) {
    String lastCommand = "/loggerget " + searchedPlayer + " " + this.getLastPage();

    TextComponent last = new TextComponent();
    last.setText("...Last");

    last.setBold(true);
    last.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, lastCommand));
    last.setHoverEvent(
        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(lastCommand).create()));

    if (this.currentPage >= this.pageLimit) {
      last.setText("Last");

    }
    if (this.currentPage + 1 == this.getLastPage()) {
      last.setUnderlined(true);

    }

    return last;
  }

  public boolean isStartOverRange() {
    return (this.currentPage > this.getFirstPage());
  }

  public boolean isEndOverRange() {
    return (this.currentPage < this.getLastPage());
  }

  public boolean isLastPage() {
    return this.getLastPage() == this.currentPage;
  }

  public boolean isFirstPage() {
    return this.getFirstPage() == this.currentPage;
  }

  /**
   * @return count
   */
  public Long getCount() {
    return this.count;
  }

  /**
   * 
   * @return number of pages
   */
  public int getNumberOfPages() {
    return (int) (count / limit);
  }

  /**
   * 
   * @return where should the pagination start
   */
  public int getPaginationStart() {
    return (this.currentPage - this.pageLimit) <= 0 ? 0 : this.currentPage - this.pageLimit;
  }

  /**
   * 
   * @return where should the pagination end
   */
  public int getPaginationEnd() {
    return (this.currentPage + this.pageLimit) <= this.getNumberOfPages()
        ? (this.currentPage + this.pageLimit)
        : this.getNumberOfPages();
  }

  /**
   * 
   * @return first page
   */
  public int getFirstPage() {
    return 1;
  }

  /**
   * 
   * @return last page
   */
  public int getLastPage() {
    return (int) Math.ceil((double) count / (double) limit);
  }

  public int getNextPage() {
    int nextPage = this.currentPage + 1;

    if (nextPage >= this.getNumberOfPages())
      return this.currentPage;

    return nextPage;
  }

  public int getPrevPage() {
    int prevPage = this.currentPage - 1;

    if (prevPage <= 0)
      return 0;
    return prevPage;
  }

  public int getNextOffset() {
    return this.limit * (this.currentPage + 1);
  }

  public int getCurrentOffset() {
    return this.limit * this.currentPage;
  }

  /**
   * 
   * @param i represents page (starting index is 0)
   * @return true if has a next page
   */
  public boolean hasNextPage(int i) {
    return this.getNumberOfPages() > i;
  }


  public static String buildCommand(String commandLabel, String searchedPlayer, int selectedPage) {
    return "/" + commandLabel + " " + searchedPlayer + " " + selectedPage;
  }

  public static TextComponent getSeperatorAsComponent() {
    return new TextComponent(" | ");
  }
}
