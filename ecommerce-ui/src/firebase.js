// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import {getStorage} from "firebase/storage";
import {getAuth,GoogleAuthProvider} from "firebase/auth";

const firebaseConfig = {
    apiKey: "AIzaSyCv4qQY0iM52S9OJbcfwCj7VKrLdMWgg1Q",
    authDomain: "marketplace-platform-mellow.firebaseapp.com",
    projectId: "marketplace-platform-mellow",
    storageBucket: "marketplace-platform-mellow.appspot.com",
    messagingSenderId: "549926331552",
    appId: "1:549926331552:web:931b5d2bb02d44eeb2d5c5",
    measurementId: "G-FZ2W15D6KK"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const auth = getAuth(app);
const ggProvider = new GoogleAuthProvider();
export const storage = getStorage(app);
export {auth, ggProvider};