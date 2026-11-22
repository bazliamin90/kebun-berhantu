# Kebun Berhantu v1.0

A pixel-art game set in a Malay kampung orchard: collect falling fruit and dodge ghosts from Malay folklore.
Play alone with 3 bots, or online with up to 4 players.

by Bazli

---

## 1. Publish the web app (GitHub Pages)

1. Create a new **public** repository on GitHub, for example `kebun-berhantu`.
2. Upload everything in this folder to the repository root (including the hidden `.nojekyll` file and the `.github` folder).
3. In the repository, go to **Settings → Pages**.
   - **Source:** Deploy from a branch
   - **Branch:** `main`, folder `/ (root)` → **Save**
4. After a minute or two the game is live at:
   `https://bazliamin90.github.io/kebun-berhantu/`

Anyone with that link can play in their browser. On a phone they can also use **Add to Home Screen** to get an app icon.

**Updating the game:** replace `index.html` in the repository. Players get the new version the next time they open it (GitHub Pages may take up to ~10 minutes to refresh).

---

## 2. Android app (APK shell)

The Android app is a full-screen "shell" that opens your GitHub Pages link.
**Every update you push to the web app shows up in the installed app automatically.** You only need to rebuild the APK if you change the app's name, icon or web address.

### Your link (already set)

The app opens `https://bazliamin90.github.io/kebun-berhantu/`, set in
`android/app/src/main/res/values/strings.xml`. If you name the repository something other than
`kebun-berhantu`, change the last part of that link to match (keep the `/` at the end).

### Build the APK on GitHub (no Android Studio needed)

1. Push this folder to GitHub. The workflow **Build Android APK** starts automatically
   (or run it yourself: **Actions → Build Android APK → Run workflow**).
2. When it finishes (about 3–5 minutes), open the run and download **KebunBerhantu-APK** under *Artifacts*.
3. Unzip it to get `KebunBerhantu-v1.0.apk`.

### Install on a phone

1. Copy the APK to the phone (or download it there).
2. Open it. Android will ask to allow installing apps from this source: allow it.
3. Open **Kebun Berhantu**.

The APK is signed with a debug key, which is fine for sharing with friends. Publishing on the Google Play Store needs a release signing key; that can be added later.

### Building in Android Studio instead

Open the `android` folder in Android Studio and choose **Build → Build APK(s)**.

### What the app shell does

- Full-screen, keeps the screen awake while playing, any orientation.
- Back button: goes back to the menu; ignored during a round so you don't quit by accident.
- No internet: shows a "Tiada sambungan internet" page with a **CUBA LAGI** (retry) button.
- Links to other websites open in the phone's browser.

---

## 3. Online multiplayer outside Claude

When the game runs from GitHub Pages or the APK, online play connects players directly to each other (WebRTC, using the free public PeerJS service for the first handshake).

- The **host's device runs the game**, so the host should have the best connection and keep the app open.
- Most home Wi-Fi and mobile networks work. Some strict networks (some offices, schools or mobile carriers) block direct connections; if a friend can't join, try another network or mobile hotspot.
- The free PeerJS service is a shared community server with no uptime guarantee.

---

## Files

| Path | What it is |
|---|---|
| `index.html` | The whole game (single file) |
| `manifest.webmanifest`, `icons/` | App name and icons for "Add to Home Screen" |
| `.nojekyll` | Tells GitHub Pages to serve files as they are |
| `android/` | Android app shell project |
| `.github/workflows/build-apk.yml` | Builds the APK on GitHub |
