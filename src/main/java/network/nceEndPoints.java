package network;

public interface nceEndPoints {
    String LOGIN = "/api/auth/session";
    String GET_MENU_ITEMS = "/term/v1/menu-items";
    String GET_TERM_DATA = "/term/v1/filters/term-data";
    String GET_TERM_SCORE = "/term/v1/term-data/stats/termScore";
    String GET_TEACHERS= "/authenticate-irp/users/role/teachers";
    String GET_USERS ="/authenticate-irp/users/";
    String GET_ASSESSMENT ="/term/v1/meta-data/max-assessments";


}
