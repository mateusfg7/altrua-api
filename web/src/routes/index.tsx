import { createFileRoute } from "@tanstack/react-router";
import { HeroSection } from "~/components/hero-section";
import { OngSection } from "~/components/ong-section";

export const Route = createFileRoute("/")({ component: App });

function App() {
  return (
    <div className="space-y-20">
      <HeroSection />
      <OngSection />
    </div>
  );
}
