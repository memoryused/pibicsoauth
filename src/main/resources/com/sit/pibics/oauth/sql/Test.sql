/*------------------------------------------------------------------------------------------------------------------------
SQL : ตรวจสอบ clientID _SQL 
Description : 
-------------------------------------------------------------------------------------------------------------------------*/
searchCountAuthorizeByClientId#Ver.1#{
	SELECT COUNT(*) AS CNT FROM [CON].CON_AUTHORIZE WHERE CLIENT_ID = ? AND ACTIVE = 'Y'
}

/*------------------------------------------------------------------------------------------------------------------------
SQL : ตรวจสอบค่าใน SEC_AUTHORIZE _SQL
Description : 
-------------------------------------------------------------------------------------------------------------------------*/
searchAuthorizeByClientId#Ver.1#{
	SELECT AUTHORIZE_KEY, CLIENT_ID, SECRET_KEY, SALT, TOKEN, ENCRYPT_DATA, GRANT_TYPE, SYSTEM_NAME, CLIENT_NAME, ACTIVE 
	FROM [CON].sec_authorize 
	WHERE CLIENT_ID = ? 
	AND SECRET_KEY = ?
	AND GRANT_TYPE = ?
	AND ACTIVE = 'Y'
}

searchAuthorizeByEncryptData#Ver.1#{
	SELECT * FROM [CON].sec_authorize WHERE ENCRYPT_DATA = ? AND ACTIVE = 'Y'
}

-------------------------------------Mockup--------------------------------------------
searchSeqAuthKey#Ver.1#{
	select max(AUTHORIZE_KEY)+1 AS AUTHORIZE_KEY from [CON].sec_authorize
}

/*------------------------------------------------------------------------------------------------------------------------
SQL : บันทึกค่า client id และ secret key _SQL 
Description : 
-------------------------------------------------------------------------------------------------------------------------*/
registerAuth#Ver.1#{
	INSERT INTO [CON].sec_authorize
	(AUTHORIZE_KEY, CLIENT_ID, SECRET_KEY, SALT, TOKEN, ENCRYPT_DATA, GRANT_TYPE, SYSTEM_NAME, CLIENT_NAME, ACTIVE)
	VALUES(
	?, 
	?,
	?,
	?, 
	NULL, 
	?, 
	?, 
	?, 
	?, 
	'Y')
}

/*------------------------------------------------------------------------------------------------------------------------
SQL : ตรวจสอบค่าใน SEC_AUTHORIZE2 _SQL
Description : 
-------------------------------------------------------------------------------------------------------------------------*/
searchCountAuthorizeByAuthorizeKey#Ver.1#{
	SELECT COUNT(*) AS CNT FROM [CON].sec_authorize WHERE AUTHORIZE_KEY = ?
}

/*------------------------------------------------------------------------------------------------------------------------
SQL : บันทึกยกเลิกใช้งาน _SQL
Description : 
-------------------------------------------------------------------------------------------------------------------------*/
updateCancelAuthorize#Ver.1#{
	UPDATE [CON].sec_authorize 
	SET ACTIVE = 'N' WHERE AUTHORIZE_KEY = ?
}

/*------------------------------------------------------------------------------------------------------------------------
SQL : บันทึกค่า token _SQL 
Description : 
-------------------------------------------------------------------------------------------------------------------------*/
updateToken#Ver.1#{
	UPDATE [CON].sec_authorize
	SET TOKEN = ?
	WHERE AUTHORIZE_KEY = ?
}