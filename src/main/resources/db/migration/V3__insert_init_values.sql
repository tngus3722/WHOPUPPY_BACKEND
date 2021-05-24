INSERT IGNORE INTO whopuppy.report_type(id,type) VALUES(1, "욕설/비하");
INSERT IGNORE INTO whopuppy.report_type(id,type) VALUES(2, "중복/사칭");
INSERT IGNORE INTO whopuppy.report_type(id,type) VALUES(3, "허위/부적절한 정보");
INSERT IGNORE INTO whopuppy.report_type(id,type) VALUES(4, "광고");
INSERT IGNORE INTO whopuppy.report_type(id, type) VALUES(5, "음란물");

INSERT IGNORE INTO whopuppy.report_division(id, division) VALUES (1,"분양커뮤니티");
INSERT IGNORE INTO whopuppy.report_division(id ,division) VALUES (2, "수제간식레시피");


INSERT IGNORE INTO whopuppy.admin_role(id,role) VALUES(1,"ROOT");
INSERT IGNORE INTO whopuppy.admin_role(id, role) VALUES(2,"MANAGER");
INSERT IGNORE INTO whopuppy.admin_role(id, role) VALUES(3, "NORMAL");

INSERT IGNORE whopuppy.admin_authority(id, authority) VALUES(1, "WANT_DO_ADOPT");
INSERT IGNORE whopuppy.admin_authority(id, authority) VALUES(2, "WANT_TAKE_ADOPT");
INSERT IGNORE whopuppy.admin_authority(id, authority) VALUES(3, "ADOPT_REVIEW");
INSERT IGNORE whopuppy.admin_authority(id, authority) VALUES(4, "SNACK");


INSERT IGNORE whopuppy.board(id, board) VALUES(1, "분양해요");
INSERT IGNORE whopuppy.board(id, board) VALUES(2, "분양합니다");
INSERT IGNORE whopuppy.board(id, board) VALUES(3, "분양후기");