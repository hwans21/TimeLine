package TwitchToYoutube.timeline.entity;

public class PageVO {
    private int currentPage;
    private int pageOfCount;
    private boolean isPrev;
    private boolean isNext;

    public boolean isPrev() {
        return isPrev;
    }

    public void setPrev(boolean prev) {
        isPrev = prev;
    }

    public boolean isNext() {
        return isNext;
    }

    public void setNext(boolean next) {
        isNext = next;
    }

    public PageVO(int currentPage, int pageOfCount) {
        this.currentPage = currentPage;
        this.pageOfCount = pageOfCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageOfCount() {
        return pageOfCount;
    }

    public void setPageOfCount(int pageOfCount) {
        this.pageOfCount = pageOfCount;
    }
}
