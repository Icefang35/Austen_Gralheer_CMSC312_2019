public class PageTable {
    Page[] pages;

    public PageTable(int size){
        pages = new Page[size];

        for(int i = 0; i < pages.length; i++){
            pages[i] = new Page(-1, -1, 'A');
        }
    }

    public void setPage(Page page){
        boolean pageExists = false;
        int replacePage = 0;
        for (int i = 0; i < pages.length; i++){
            if(pages[i].pageID == page.pageID){
                pageExists = true;
                replacePage = i;
                break;
            }
        }
        if(pageExists){
            pages[replacePage] = page;
        } else{
            for (int i = 0; i < pages.length; i++){
                if(pages[i].pageID == -1){
                    pages[i] = page;
                    break;
                }
            }
        }
    }

    public int length(){
        return pages.length;
    }
}
