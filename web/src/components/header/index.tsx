import { FavouriteIcon, Login01Icon } from "@hugeicons/core-free-icons";
import { HugeiconsIcon } from "@hugeicons/react";
import { Link } from "@tanstack/react-router";
import { Button } from "../ui/button";

export function Header() {
  return (
    <header className="sticky top-0 border-muted border-b bg-background/95 py-3 backdrop-blur-md supports-backdrop-filter:bg-background/60">
      <div className="mx-auto grid max-w-6xl grid-cols-4">
        <Link className="flex items-center gap-2" to="/">
          <div className="flex size-9 items-center justify-center rounded-lg bg-primary">
            <HugeiconsIcon
              className="size-5 text-primary-foreground"
              icon={FavouriteIcon}
            />
          </div>
          <span className="font-bold text-xl tracking-tight">Altrua</span>
        </Link>
        <nav className="col-span-2 hidden items-center justify-center gap-6 md:flex">
          <a
            className="font-medium text-muted-foreground text-sm transition-colors hover:text-foreground"
            href="#ongs"
          >
            ONGs
          </a>
          <a
            className="font-medium text-muted-foreground text-sm transition-colors hover:text-foreground"
            href="#eventos"
          >
            Eventos
          </a>
          <a
            className="font-medium text-muted-foreground text-sm transition-colors hover:text-foreground"
            href="#como-funciona"
          >
            Como Funciona
          </a>
        </nav>

        <div className="hidden items-center justify-end gap-3 md:flex">
          <Button size="sm" variant="ghost">
            <HugeiconsIcon icon={Login01Icon} />
            Entrar
          </Button>
          <Button size="sm">Cadastre-se</Button>
        </div>
      </div>
    </header>
  );
}
