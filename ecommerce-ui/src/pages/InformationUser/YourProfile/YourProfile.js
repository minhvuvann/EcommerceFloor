import React, {useContext, useEffect, useState} from "react";
import classNames from "classnames/bind";
import {
    Container,
    Grid,
    RadioGroup,
    Radio,
    FormControlLabel,
} from "@mui/material";

import styles from "./YourProfile.module.scss";
import UpLoadFileImage from "~/components/UploadFileImage";
import {useMediaQuery} from "react-responsive";
import {getDownloadURL, ref, uploadBytes} from "firebase/storage";
import {storage} from "~/firebase";
import {nanoid} from "nanoid";
import {updateInfoUser} from "~/services/workspaces.sevices";
import {format} from "date-fns";
import {UserContext} from "~/config/provider/UserProvider";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function YourProfile(props) {
    const [gender, setGender] = useState("");
    const [images, setImages] = useState([]);
    const [open, setOpen] = useState(true);
    const [fullName, setFullName] = useState();
    const [email, setEmail] = useState();
    const [telephone, setTelephone] = useState();
    const [birthday, setBirthday] = useState();
    const [showButton, setShowButton] = useState(true)
    const maxNumber = 1;
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const {setShouldUpdate} = useContext(UserContext);
    const {localeList} = useContext(LanguageContext);
    useEffect(() => {
        async function loadData() {
            setEmail(rsUser.email);
            setTelephone(rsUser.telephone);
            setFullName(rsUser.fullName);
            setGender(rsUser.gender);
            setBirthday(format(rsUser.birthday ? rsUser.birthday : new Date(), 'yyyy-MM-dd'))
        }

        loadData();
    }, []);
    const handleGender = (event) => {
        console.log(gender);
        setGender(event.target.value);
    };
    const handleFullName = (event) => {
        setFullName(event.target.value);
    }
    const handleEmail = (event) => {
        setEmail(event.target.value);
    }
    const handlePhone = (event) => {
        setTelephone(event.target.value);
    }
    const handleBirthDay = (event) => {
        setBirthday(event.target.value);
    }

    const onChange = (imageList, addUpdateIndex) => {
        setImages(imageList);
        setShowButton(false)
        setOpen(false);
    };
    const uploadImgFirebase = async () => {
        const imgRef = ref(storage, `images/${rsUser.id + '_' + nanoid()}`);
        if (images && images.length !== 0) {
            uploadBytes(imgRef, images[0].file).then(response => {
                getDownloadURL(response.ref).then(async (url) => {
                    const body = {
                        gender: gender,
                        telephone: telephone,
                        email: email,
                        fullName: fullName,
                        birthday: birthday,
                        imageUrl: url

                    }
                    const userData = await updateInfoUser(rsUser.id, body);
                    setShouldUpdate(prev => !prev);

                })
            });
        }
        const body = {
            gender: gender,
            telephone: telephone,
            email: email,
            fullName: fullName,
            birthday: birthday,
        }
        console.log(gender);
        const userData = await updateInfoUser(rsUser.id, body);
        setShouldUpdate(prev => !prev);
    }
    const isTablet = useMediaQuery({minWidth: 768});
    return (
        <div className={cx("wrapper")}>
            <Container>
                <div className={cx("content-wrapper")}>
                    <Grid container>
                        <Grid item md={12} container>
                            <div className={cx("header")}>
                                <h3 className={cx("header-name")}>{localeList['info_my_profile']}</h3>
                                <p>{localeList['info_my_profile_manager']}</p>
                            </div>

                            <div className={cx("info-user")}>
                                <div className={cx("info-item")}>
                                    <Grid container>
                                        {isTablet && (
                                            <Grid container item md={2}>
                                                <h3 className={cx("label")}>{localeList['info_chose_image']}</h3>
                                            </Grid>
                                        )}
                                        <Grid container item md={10}>
                                            <UpLoadFileImage
                                                imageProduct={rsUser.imageUrl}
                                                showButton={showButton}
                                                open={open}
                                                buttonChose={localeList['info_chose_image']}
                                                buttonUpdate={localeList['info_update_image']}
                                                images={images}
                                                name={rsUser.username}
                                                maxNumber={maxNumber}
                                                onChange={onChange}
                                            />
                                        </Grid>
                                    </Grid>
                                </div>
                                <div className={cx("info-item")}>
                                    <Grid container>
                                        <Grid container item md={2}>
                                            <h3 className={cx("label")}>{localeList['info_name_login']}</h3>
                                        </Grid>
                                        <Grid container item md={10}>
                                            <span className={cx("username")}>{rsUser.username}</span>
                                        </Grid>
                                    </Grid>
                                </div>
                                <div className={cx("info-item")}>
                                    <Grid container>
                                        <Grid container item md={2}>
                                            <h3 className={cx("label")}>{localeList['info_name']}</h3>
                                        </Grid>
                                        <Grid container item md={10}>
                                            <input
                                                onChange={handleFullName}
                                                value={rsUser.fullName !== "" ? rsUser.fullName : localeList['info_not_found']}
                                                className={cx("input-item")}/>
                                        </Grid>
                                    </Grid>
                                </div>
                                <div className={cx("info-item")}>
                                    <Grid container>
                                        <Grid container item md={2}>
                                            <h3 className={cx("label")}>Email</h3>
                                        </Grid>
                                        <Grid container item md={10}>
                                            <input onChange={handleEmail}
                                                   value={rsUser.email !== "" ? rsUser.email : localeList['info_not_found']}
                                                   className={cx("input-item")}/>
                                        </Grid>
                                    </Grid>
                                </div>
                                <div className={cx("info-item")}>
                                    <Grid container>
                                        <Grid container item md={2}>
                                            <h3 className={cx("label")}>{localeList['info_telephone']}</h3>
                                        </Grid>
                                        <Grid container item md={10}>
                                            <input
                                                onChange={handlePhone}
                                                value={rsUser.telephone !== "" ? rsUser.telephone : localeList['info_not_found']}
                                                className={cx("input-item")}/>
                                        </Grid>
                                    </Grid>
                                </div>
                                <div className={cx("info-item")}>
                                    <Grid container alignItems="center">
                                        <Grid container item md={2}>
                                            <h3 className={cx("label")}>{localeList['info_gender']}</h3>
                                        </Grid>
                                        <Grid container item md={10}>
                                            <RadioGroup
                                                value={gender}
                                                onChange={handleGender}
                                                sx={{display: "flex", flexDirection: "row"}}
                                            >
                                                <FormControlLabel
                                                    value="MAN"
                                                    control={<Radio/>}
                                                    label={localeList['gender_men']}
                                                />
                                                <FormControlLabel
                                                    value="WOMEN"
                                                    control={<Radio/>}
                                                    label={localeList['gender_woman']}
                                                />
                                                <FormControlLabel
                                                    value="OTHER"
                                                    control={<Radio/>}
                                                    label={localeList['gender_other']}
                                                />
                                            </RadioGroup>
                                        </Grid>
                                    </Grid>
                                </div>
                                <div className={cx("info-item")}>
                                    <Grid container>
                                        <Grid container item md={2}>
                                            <h3 className={cx("label")}>{localeList['info_birthday']}</h3>
                                        </Grid>
                                        <Grid container item md={10}>
                                            <input
                                                type="date"
                                                onChange={handleBirthDay}
                                                value={birthday}
                                                className={cx("input-item")}/>
                                        </Grid>
                                    </Grid>
                                </div>
                                <div className={cx("info-item")}>
                                    <Grid container>
                                        <Grid container item md={2}/>
                                        <Grid container item md={10}>
                                            <button
                                                onClick={uploadImgFirebase}
                                                style={{
                                                    padding: "8px 34px",
                                                }} className={cx("btn-save")}>{localeList['button_save']}
                                            </button>
                                        </Grid>
                                    </Grid>
                                </div>
                            </div>
                        </Grid>
                    </Grid>
                </div>
            </Container>
        </div>
    );
}

export default YourProfile;
