/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      fontFamily: {
        sans: ["Inter", "ui-sans-serif", "system-ui", "-apple-system", "Segoe UI", "Roboto", "Ubuntu", "Cantarell", "Noto Sans", "sans-serif"],
      },
      colors: {
        brand: {
          primary: "#3b82f6", // blue-500
          secondary: "#8b5cf6", // violet-500
          accent: "#22d3ee", // cyan-400
          dark: "#0f172a", // slate-900
        }
      },
      boxShadow: {
        card: "0 8px 24px rgba(0,0,0,0.06)",
      },
      backdropBlur: {
        xs: '2px',
      }
    },
  },
  plugins: [],
}
