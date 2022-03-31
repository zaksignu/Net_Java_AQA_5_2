import lombok.AllArgsConstructor;
import lombok.Data;


    @Data
    @AllArgsConstructor
    public class UserDefine {
        private String login;
        private String password;
        private String status;

        public String SendInfo(){
            return     "{\n" +
                    "\"login\":"+"\""+this.login+"\""+ ",\n"+
                    "\"password\":"+"\""+this.password+"\""+ ",\n"+
                    "\"status\":"+"\""+this.status +"\""+ "\n"+
                    "}";
        }
    }


