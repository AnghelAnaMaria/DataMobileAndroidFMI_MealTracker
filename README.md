# Meal Tracker App

This is a mobile app for Android made with Android Studio. 
The application is a meal tracker for planning meals and tracking calories for each meal, making it easy to record nutritional values and data.

## Local setup

- Contact one of the repository owners to be added to the associated Firebase project
- Download `google-services.json` from Firebase, place under `/app`
- Obtain SHA-1 key from Android Studio and add it to Firebase Console
- Add `FDC_API_KEY` property under `local.properties`. Populate it with:
  - Either an FDC API key obtained per [FDC API Documentation](https://fdc.nal.usda.gov/api-guide)
  - Or use the placeholder `DEMO_KEY` as value, which will work without prior signup but has much more aggressive date limiting compared to an API key obtained via signup (30 requests per IP per hour and 50 per day)
- From Android Studio, set build variant as `prodDebug` for local use
- Run app via Android Studio. The app was tested with the `Pixel 9 API 36.0` device emulated.
